import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VkService {

    private JSONClass JSONClass;

    private static int currentPostsCount;

    private static int postsCount = 0;

    public VkService() throws IOException {
        String firstJsonUrlVk = new PropertiesGetter().getJsonUrlVk();
        JSONClass = getJSONClass(firstJsonUrlVk);
        currentPostsCount = JSONClass.getResponse().getCount();
    }


    public ArrayList<String> getUrlsAndTextPost() throws IOException {
        String currentJsonUrlVk = new PropertiesGetter().getJsonUrlVkWithOffset();
        //СДЕЛАТЬ НОРМАЛЬНО
        postsCount = currentPostsCount - postsCount;
        JSONClass currentJson = getJSONClass
                ("https://api.vk.com/method/wall.get?owner_id=-204646604&count=" + postsCount + currentJsonUrlVk);
        currentPostsCount = currentJson.getResponse().getCount();
        //получаем url фотографий
        ArrayList<String> urlsAndTextPost = new ArrayList<>();
        List<JSONClass.Item> items = new ArrayList<>(currentJson.getResponse().getItems());
        for(JSONClass.Item i : items){
            List<JSONClass.Attachment> attachments = i.getAttachments();
            for (JSONClass.Attachment attachment : attachments) {
                if (!"photo".equals(attachment.getType())) {
                    continue;
                }
                urlsAndTextPost.add(attachment.getPhoto().getSizes()
                        .get(attachment.getPhoto().getSizes().size() - 1).getUrl());
            }
        }
        //получаем текст к постам и добавляем в массив к url
        for (int t = 0; t < currentJson.getResponse().getItems().size(); t++) {
            urlsAndTextPost.add(currentJson.getResponse().getItems().get(t).text);
        }
        postsCount = currentPostsCount;
        return urlsAndTextPost;
    }

    public int getCurrentPostsCount() {
        return currentPostsCount;
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
