package balik.englishbot.handler;

import balik.englishbot.bot.BotCommands;
import balik.englishbot.domain.User;
import balik.englishbot.util.Dictionary;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {
    protected List<KeyboardRow> keyboard;
    protected KeyboardRow keyboardRow;
    protected Dictionary dictionary;

    public AbstractCommand() {
        dictionary = Dictionary.getInstance();
    }

    protected SendMessage createNewMessage(Update update) {
        keyboard = new ArrayList<>();

        return SendMessage.builder()
            .text("I don't understand you :(")
            .parseMode("HTML")
            .chatId(update.getMessage().getChatId().toString())
            .build();
    }

    protected void setKeyboard(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    protected abstract void processUpdate(Update update, User user, SendMessage message);

    public SendMessage handleMessage(Update update, User user) {
        SendMessage message = createNewMessage(update);
        processUpdate(update, user, message);
        setKeyboard(message);
        return message;
    }

    protected void clearGame(User user){
        user.setUnit(0);
        user.setCurrentQuestion(0);
        user.setScore(0);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(BotCommands.START_GAME.getValue()));
        keyboard.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(BotCommands.LIST.getValue()));
        keyboard.add(keyboardRow);
    }
}
