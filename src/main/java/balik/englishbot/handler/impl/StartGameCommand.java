package balik.englishbot.handler.impl;

import balik.englishbot.bot.Messages;
import balik.englishbot.handler.AbstractCommand;
import balik.englishbot.handler.Commands;
import balik.englishbot.domain.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class StartGameCommand extends AbstractCommand {

    @Override
    protected void processUpdate(Update update, User user, SendMessage message) {
        if (user.getCurrentQuestion() != 0) {
            return;
        }

        user.setCurrentQuestion(1);

        message.setText(String.format(Messages.TASK.getMessage(),
                dictionary.getKeyByIndex(user.getCurrentQuestion())));

        keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(Commands.FINISH_GAME.getValue()));
        keyboard.add(keyboardRow);
    }
}
