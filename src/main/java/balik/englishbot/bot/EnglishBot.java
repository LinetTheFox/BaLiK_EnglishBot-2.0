package balik.englishbot.bot;

import balik.englishbot.database.User;
import balik.englishbot.database.UserDAO;
import balik.englishbot.dict.Dictionary;
import balik.englishbot.rank.RankMaker;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EnglishBot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private final UserDAO userRepo;

    /**
     * COMMANDS
     **/
    private static final String START = "/start";
    private static final String HELP = "HELP";
    private static final String START_GAME = "START GAME";
    private static final String FINISH_GAME = "FINISH GAME";
    private static final String LIST = "WORD LIST";


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

        processUpdate(update, user);

        userRepo.updateUser(user);
    }

    private void processUpdate(Update update, User user) {
        boolean advertising = false;

        SendMessage message = new SendMessage()
                .setText("I don't understand you:(")
                .setChatId(update.getMessage().getChatId())
                .setParseMode("HTML");

        final String request = update.getMessage().getText();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow;

        Dictionary dictionary = Dictionary.getInstance();

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

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(START_GAME));
                keyboard.add(keyboardRow);

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(LIST));
                keyboard.add(keyboardRow);

                break;
            case HELP:
                //todo: help logic
                break;
            case START_GAME:
                if (user.getCurrentQuestion() != 0) {
                    break;
                }

                user.setCurrentQuestion(user.getCurrentQuestion() + 1);

                message.setText(String.format(Messages.TASK.getMessage(),
                        dictionary.getKeyByIndex(user.getCurrentQuestion())));

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(FINISH_GAME));
                keyboard.add(keyboardRow);

                break;
            case FINISH_GAME:
                if (user.getCurrentQuestion() == 0) {
                    break;
                }

                advertising = true;

                String playerRank = RankMaker.determineRank(user.getScore(), user.getCurrentQuestion() - 1);
                message.setText(playerRank);

                user.setUnit(0);
                user.setCurrentQuestion(0);
                user.setScore(0);

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(START_GAME));
                keyboard.add(keyboardRow);

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(LIST));
                keyboard.add(keyboardRow);

                break;
            case LIST:
                if (user.getCurrentQuestion() != 0) {
                    break;
                }

                message.setText(dictionary.getDictionaryAsString());

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(START_GAME));
                keyboard.add(keyboardRow);

                break;
            default:
                if (user.getCurrentQuestion() != 0) {
                    String answer = dictionary.getAnswer(dictionary.getKeyByIndex(user.getCurrentQuestion()));
                    if (answer.replace('-', ' ').equals
                            (request.toLowerCase().replace('-', ' '))) {

                        message.setText(Messages.CORRECT.getMessage());
                        user.setScore(user.getScore() + 1);

                    } else {
                        message.setText(String.format(Messages.WRONG.getMessage(), answer));
                    }

                    user.setCurrentQuestion(user.getCurrentQuestion() + 1);

                    if (user.getCurrentQuestion() != dictionary.getSize() + 1) {
                        message.setText(message.getText() + String.format(Messages.TASK.getMessage(),
                                dictionary.getKeyByIndex(user.getCurrentQuestion())));

                        keyboardRow = new KeyboardRow();
                        keyboardRow.add(new KeyboardButton(FINISH_GAME));
                        keyboard.add(keyboardRow);
                    } else {
                        advertising = true;

                        String rank = RankMaker.determineRank(user.getScore(), dictionary.getSize());
                        message.setText(message.getText() + rank);

                        user.setUnit(0);
                        user.setCurrentQuestion(0);
                        user.setScore(0);

                        keyboardRow = new KeyboardRow();
                        keyboardRow.add(new KeyboardButton(START_GAME));
                        keyboard.add(keyboardRow);

                        keyboardRow = new KeyboardRow();
                        keyboardRow.add(new KeyboardButton(LIST));
                        keyboard.add(keyboardRow);
                    }
                }
        }

        //KEYBOARD ADDING
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        replyKeyboardMarkup.setKeyboard(keyboard);

        executeMessage(message);

        if (advertising) {
            SendMessage advertisingMessage = new SendMessage()
                    .setText(Messages.SUBSCRIBE.getMessage())
                    .setChatId(update.getMessage().getChatId())
                    .setParseMode("HTML");
            executeMessage(advertisingMessage);
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
}
