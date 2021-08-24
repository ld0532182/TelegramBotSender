import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TelegramBotMain extends TelegramLongPollingBot {
    private PropertiesGetter prop = new PropertiesGetter();
    private TelegramService telegramSender = new TelegramService();
    private boolean subscribersThread = false;
    private HashMap<Long, Message> subscribers = new HashMap<>();

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
        //запускаем поток в цикле, чтобы он периодически проверял новые посты без вмешательства пользователя
        if (!subscribersThread) {
            subscribersThread = true;
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        if (!subscribers.isEmpty()) {
                            for (Map.Entry<Long, Message> entry : subscribers.entrySet()) {
                                Message value = entry.getValue();
                                executePhoto(value);
                            }
                            telegramSender.commitAddedPosts();
                        }
                    } catch (TelegramApiException | IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(25000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        try { //получаем сообщение от пользователя и анализируем его
            switch (message.getText()) {
                case "/help" -> execute(telegramSender.sendMessage(message, "/help"));
                case "/info" -> execute(telegramSender.sendMessage(message, "/info"));
                case "/subscribe" -> {
                    //проверяем, подписывался ли пользователь, если нет, добавляем его в 'subscribers'
                    if (!subscribers.containsKey(message.getChatId())) {
                        subscribers.put(message.getChatId(), message);
                        execute(telegramSender.sendMessage(message, "subscribe"));
                    } else {
                        execute(telegramSender.sendMessage(message, "alreadySubscribed"));
                    }
                }
                case "/unsubscribe" -> {
                    subscribers.remove(message.getChatId());
                    execute(telegramSender.sendMessage(message, "unsubscribe"));
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executePhoto(Message message) throws TelegramApiException, IOException {

        ArrayList<SendPhoto> photos = telegramSender.sendPhoto(message);
        if (photos == null) {
            return;
        }
        for (SendPhoto s : photos) {
            execute(s);
        }
    }
}








