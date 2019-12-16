package balik.englishbot.bot.command.impl;

import balik.englishbot.bot.command.AbstractCommand;
import balik.englishbot.bot.command.Commands;
import balik.englishbot.domain.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ListGameCommand extends AbstractCommand {

    @Override
    protected void processUpdate(Update update, User user, SendMessage message) {
        if (user.getCurrentQuestion() != 0) {
            return;
        }

        message.setText(dictionary.getDictionaryAsString());

        keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(Commands.START_GAME.getValue()));
        keyboard.add(keyboardRow);
    }
}
