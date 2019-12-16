package balik.englishbot.bot;

import balik.englishbot.bot.command.CommandHandler;
import balik.englishbot.dao.UserDAO;
import balik.englishbot.domain.User;
import balik.englishbot.dao.impl.UserDAOImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;


//todo: service layer
public class EnglishBot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private final UserDAO userRepo;

    private static final Logger LOG = Logger.getLogger(EnglishBot.class);

    public EnglishBot() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bot.properties");
        Properties properties = new Properties();
        properties.load(Objects.requireNonNull(inputStream));
        BOT_USERNAME = properties.getProperty("bot.name");
        BOT_TOKEN = properties.getProperty("bot.token");
        userRepo = UserDAOImpl.getInstance();
        LOG.info("Username and token entered");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!(update.hasMessage() && update.getMessage().hasText())) {
            return;
        }

        User user = userRepo.getUser(update.getMessage().getChatId());

        if (user == null) {
            user = new User(update.getMessage().getChatId());
            user.setUsername(update.getMessage().getFrom().getUserName());
            user.setName(update.getMessage().getFrom().getFirstName());
            LOG.info("Adding new user with chatId: " + user.getChatId());
            userRepo.createUser(user);
        }

        processUpdate(update, user);

        userRepo.updateUser(user);
    }

    private void processUpdate(Update update, User user) {
        final String request = update.getMessage().getText();
        SendMessage message = CommandHandler.getInstance().getCommand(request).handleMessage(update,user);
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
