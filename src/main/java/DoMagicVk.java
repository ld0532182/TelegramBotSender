import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DoMagicVk {

    private final String jsonUrlVk;

    private final Root root;

    private final int currentPostsCount;


    public DoMagicVk(){
        jsonUrlVk = new PropertiesGetter().getJsonUrlVk();
        root = getRoot();
        currentPostsCount = root.getResponse().getCount();
    }


    public List<String> getUrlsOfBiggestPhotos() {
        List<String> urlsString = new ArrayList<>();
        List<Root.Attachment> attachments = new ArrayList<>(root.getResponse().getItems().get(0).getAttachments());
        for (Root.Attachment attachment : attachments) {
            if (!"photo".equals(attachment.getType())) {
                continue;
            }
            urlsString.add(attachment.getPhoto().getSizes()
                    .get(attachment.getPhoto().getSizes().size()-1).getUrl());
        }
        return urlsString;
    }

    private Root getRoot() {
        Root root = null;
        try {
            URL url = new URL(jsonUrlVk);
            Scanner in = new Scanner((InputStream) url.getContent());
            String result = "";
            while (in.hasNext()) {
                result += in.nextLine();
            }
            ObjectMapper om = new ObjectMapper();
            root = om.readValue(result, Root.class);
        } catch (IOException | NullPointerException e) {
            System.out.println("Error parsing Vk JSON");
            e.printStackTrace();
        }
        return root;
    }

    public int getCurrentPostsCount() {
        return currentPostsCount;
    }
}
