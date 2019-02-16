package balik.englishbot.bot;

import balik.englishbot.database.User;
import balik.englishbot.database.UserDAO;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnglishBot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private final UserDAO userRepo;


    private static final Logger LOG = Logger.getLogger(EnglishBot.class);

    public EnglishBot() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bot.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        BOT_USERNAME = properties.getProperty("bot.name");
        BOT_TOKEN = properties.getProperty("bot.token");
        userRepo = UserDAO.getInstance();
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

        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText() + " ," + user.getName());
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
