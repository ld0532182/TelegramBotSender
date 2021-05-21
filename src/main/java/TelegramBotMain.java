import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBotMain extends TelegramLongPollingBot {
    PropertiesGetter prop = new PropertiesGetter();
    DoMagicTelegram telegramSender = new DoMagicTelegram();

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
        return prop.getTokenTelegram();
    }

    @Override
    public void onUpdateReceived(Update update) {


        Message message = update.getMessage();
        try {
            switch (message.getText()) {
                case "/help" -> execute(telegramSender.sendMessage(message, "/help"));
                case "/info" -> execute(telegramSender.sendMessage(message, "/info"));
                case "/check" -> execute(telegramSender.sendMessage(message, "/check"));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}








