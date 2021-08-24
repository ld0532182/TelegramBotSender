import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Класс парсит JSON запрос в объет и производит выборку новых постов, формируя из них хешсет.
 * Он также сохраняет хешсет номеров уже добавленных постов. VK дает каждому посту уникальный номер, начиная с 1..2..3
 */
public class VkService {

    private static int currentPostsCount;
    private static HashSet<Integer> addedPosts = new HashSet<>();
    private static HashSet<Integer> tmpAddedPosts = new HashSet<>();
    private static int postsCount = 0;

    public VkService() throws IOException {
        String firstJsonUrlVk = new PropertiesGetter().getJsonUrlVk();
        JSONClass jsonClass = getJSONClass(firstJsonUrlVk);
        currentPostsCount = jsonClass.getResponse().getCount();
    }

    //здесь мы получаем список url, в связке фото + текст
    public ArrayList<String> getUrlsAndTextPost() throws IOException {
        postsCount = currentPostsCount - postsCount;
        //наш url json запрос включает : id группы + отступ кол-ва постов + токен.
        //Мы устанавливаем отступ, чтобы убрать из обработки посты, которые уже отправлялись пользователям.
        String currentJsonUrlVk = (new PropertiesGetter().getJsonUrlVk().
                replaceFirst("offset=0", "offset="+postsCount));
        JSONClass currentJson = getJSONClass(currentJsonUrlVk);
        currentPostsCount = currentJson.getResponse().getCount();

        ArrayList<String> urlsAndTextPost = new ArrayList<>();
        //на основании текущего json объекта получаем итемы (они же посты в группе) и проверяем на оригинальность
        List<JSONClass.Item> items = new ArrayList<>(currentJson.getResponse().getItems());
        for (JSONClass.Item item : items) {
            if (addedPosts.contains(item.id)) {
                continue;
            }
            tmpAddedPosts.add(item.id);
            //получаем вложения и проверяем, чтобы это было фото, если это так,
            //то вытаскиваем самый большой размер фото
            List<JSONClass.Attachment> attachments = item.getAttachments();
            for (JSONClass.Attachment attachment : attachments) {
                if (!"photo".equals(attachment.getType())) {
                    continue;
                }
                urlsAndTextPost.add(attachment.getPhoto().getSizes()
                        .get(attachment.getPhoto().getSizes().size() - 1).getUrl());
            }
            //добавляем в массив текст к посту
            urlsAndTextPost.add(item.text);
        }
        postsCount = currentPostsCount;
        return urlsAndTextPost;
    }

    private JSONClass getJSONClass(String currentJsonUrlVk) throws IOException {
        JSONClass jsonClass = null;
        try {
            URL url = new URL(currentJsonUrlVk);
            Scanner in = new Scanner((InputStream) url.getContent());
            StringBuilder result = new StringBuilder();
            while (in.hasNext()) {
                result.append(in.nextLine());
            }
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            jsonClass = om.readValue(result.toString(), JSONClass.class);
        } catch (NullPointerException e) {
            System.out.println("Error parsing Vk JSON");
            e.printStackTrace();
        }
        return jsonClass;
    }

    public void commitAddedPosts() {
        addedPosts.addAll(tmpAddedPosts);
        tmpAddedPosts.clear();
    }
}
