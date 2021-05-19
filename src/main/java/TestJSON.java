import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class TestJSON {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://api.vk.com/method/wall.get?owner_id=-204646604&offset=1&access_token=d1aca3b8d1aca3b8d1aca3b879d1db4539dd1acd1aca3b8b10f7a75b9aadea6c8547fc5&v=5.130");
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        ObjectMapper om = new ObjectMapper();
        Root root = om.readValue((result), Root.class);
        System.out.println(root.getResponse().getCount());




    }
}
