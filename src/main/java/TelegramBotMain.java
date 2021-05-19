import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class TelegramBotMain extends TelegramLongPollingBot {

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBotMain());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "MyFistBot112233_bot";
    }

    @Override
    public String getBotToken() {
        return Token.TOKEN_TELEGRAM.stringNumber;
    }

    @Override
    public void onUpdateReceived(Update update) {


        Message message = update.getMessage();
        try {
            if ("/help".equals(message.getText())) {
                String text = """
                        /help - show commands
                        /info - show info about bot
                        /check - use this command to check new posts""";
                execute(getSendMessage(message, text));

            }
            if ("/info".equals(message.getText())) {
                String text = "I check new posts from vk.com group and send them in this chat.";
                execute(getSendMessage(message, text));
            }
            if ("/check".equals(message.getText())) {

                String text = "Checking posts...";
                execute(getSendMessage(message, text));
                execute(getSendPhoto(message, getRoot().getResponse().getItems().get(0)
                        .getAttachments().get(0).getPhoto().getSizes().get(9).getUrl()));

            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    private SendPhoto getSendPhoto(Message message, String photoPath) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(message.getChatId().toString());
        photo.setPhoto(new InputFile(photoPath));
        return photo;
    }

    private SendMessage getSendMessage(Message message, String text) {
        SendMessage mess = new SendMessage();
        mess.setChatId(message.getChatId().toString());
        mess.setText(text);
        return mess;
    }

    private static Root getRoot() throws IOException {
        URL url = new URL("https://api.vk.com/method/wall.get?owner_id=-204646604&offset=1&access_token=d1aca3b8d1aca3b8d1aca3b879d1db4539dd1acd1aca3b8b10f7a75b9aadea6c8547fc5&v=5.130");
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        ObjectMapper om = new ObjectMapper();
        return om.readValue((result), Root.class);
    }
}




