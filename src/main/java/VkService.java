import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class VkService {

    private static int currentPostsCount;
    private static HashSet<Integer> addedPosts = new HashSet<>();
    private static int postsCount = 0;

    public VkService() throws IOException {
        String firstJsonUrlVk = new PropertiesGetter().getJsonUrlVk();
        JSONClass jsonClass = getJSONClass(firstJsonUrlVk);
        currentPostsCount = jsonClass.getResponse().getCount();
    }


    public ArrayList<String> getUrlsAndTextPost() throws IOException {
        String currentJsonUrlVk = new PropertiesGetter().getJsonUrlVkWithOffset();
        //СДЕЛАТЬ НОРМАЛЬНО
        postsCount = currentPostsCount - postsCount;
        JSONClass currentJson = getJSONClass
                ("https://api.vk.com/method/wall.get?owner_id=-204646604&count=" + postsCount + currentJsonUrlVk);
        currentPostsCount = currentJson.getResponse().getCount();
        //получаем url фотографий и проверяем оригинальность постов
        ArrayList<String> urlsAndTextPost = new ArrayList<>();
        List<JSONClass.Item> items = new ArrayList<>(currentJson.getResponse().getItems());
        //посмотреть, что добавляется в urlsAndTextPost, если посты уже отправлялись.
        for(JSONClass.Item item : items){
            if (addedPosts.contains(item.id)) {
                continue;
            }
            addedPosts.add(item.id);
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
}
