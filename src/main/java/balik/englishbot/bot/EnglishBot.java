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

    /**
     * COMMANDS
     **/
    private static final String START = "/start";
    public static final String START_GAME = "START GAME";
    public static final String FINISH_GAME = "FINISH GAME";
    public static final String SELECT_UNIT = "SELECT UNIT";
    public static final String LIST = "WORD LIST";


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

        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode("HTML");

        processUpdate(update, message, user);

        userRepo.updateUser(user);
    }

    private void processUpdate(Update update, SendMessage message, User user) {
        final String request = update.getMessage().getText();

        switch (request) {
            case START:
                if (user.getName() == null) {
                    message.setText(String.format(Messages.START.getMessage(), "noname"));
                } else {
                    message.setText(String.format(Messages.START.getMessage(), user.getName()));
                }
                user.setUnit(0);
                user.setCurrentQuestion(0);
                user.setScore(0);
                executeMessage(message);
                break;
            case SELECT_UNIT:
                break;
            case START_GAME:
                break;
            case FINISH_GAME:
                break;
            case LIST:
                break;
            default:
        }
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

    //todo:ON CLOSING CONNECTION SHUT DOWN
}
