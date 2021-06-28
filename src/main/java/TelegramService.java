import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;

public class TelegramService {

    public ArrayList<SendPhoto> sendPhoto(Message message) throws IOException {
        ArrayList<SendPhoto> photos = new ArrayList<>();
        VkService vkService = new VkService();
        ArrayList<String> getUrlsAndTextPost = vkService.getUrlsAndTextPost();
        if (getUrlsAndTextPost.isEmpty()) {
            return null;
        }
        //получаем url фотографий и также текст к ним, путем прибавления к "i" результата деления размера массива на 2.
        for (int i = 0; i < getUrlsAndTextPost.size() - 1; i++ ) {
            //каждое второе значение это текст к фотографии, его пропускаем, оставляя только url
            if(i % 2 != 0){
                continue;
            }
            SendPhoto photo = new SendPhoto();
            photo.setChatId(message.getChatId().toString());
            photo.setPhoto(new InputFile(getUrlsAndTextPost.get(i)));
            photo.setCaption(getUrlsAndTextPost.get(i+1));
            photos.add(photo);
        }
        return photos;
    }

    public SendMessage sendMessage(Message message, String keyWord) {
        String messageText;
        SendMessage mess = new SendMessage();
        mess.setChatId(message.getChatId().toString());
        if ("/help".equals(keyWord)) {
            messageText = """
                    /help - show commands
                    /info - show info about bot
                    /check - use this command to check new posts""";
            mess.setText(messageText);
        }
        if ("/info".equals(keyWord)) {
            messageText = "I check new posts from vk.com group and send them in this chat.";
            mess.setText(messageText);
        }
        if ("/check".equals(keyWord)) {
            messageText = "Checking posts...";
            mess.setText(messageText);
        }
        if ("noNewPosts".equals(keyWord)) {
            messageText = "No new posts found";
            mess.setText(messageText);
        }
        return mess;
    }


}
