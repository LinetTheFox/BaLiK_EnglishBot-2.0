package balik.englishbot.bot;

import balik.englishbot.handler.CommandHandler;
import balik.englishbot.domain.User;
import balik.englishbot.service.UserService;
import balik.englishbot.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;


public class EnglishBot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private final UserService userService;

    private static final Logger LOG = Logger.getLogger(EnglishBot.class);

    public EnglishBot() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bot.properties");
        Properties properties = new Properties();
        properties.load(Objects.requireNonNull(inputStream));
        BOT_USERNAME = properties.getProperty("bot.name");
        BOT_TOKEN = properties.getProperty("bot.token");
        userService = new UserServiceImpl();
        LOG.info("Username and token entered");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!(update.hasMessage() && update.getMessage().hasText())) {
            return;
        }

        User user = userService.getUser(update);

        processUpdate(update, user);

        userService.updateUser(user);
    }

    private void processUpdate(Update update, User user) {
        final String request = update.getMessage().getText();
        SendMessage message = CommandHandler.getInstance().getCommand(request).handleMessage(update, user);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            LOG.error("Send message error!\n" + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
