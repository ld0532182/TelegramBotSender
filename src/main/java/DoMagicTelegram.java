import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DoMagicTelegram {


    private SendPhoto SendPhoto(Message message, String photoPath) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(message.getChatId().toString());
        photo.setPhoto(new InputFile(photoPath));
        return photo;
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
        if ("/info".equals(keyWord)){
            messageText = "I check new posts from vk.com group and send them in this chat.";
            mess.setText(messageText);
        }
        if ("/check".equals(keyWord)){
            messageText = "Checking posts...";
            mess.setText(messageText);
        }
        return mess;
    }


}
