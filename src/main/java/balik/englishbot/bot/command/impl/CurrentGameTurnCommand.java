package balik.englishbot.bot.command.impl;

import balik.englishbot.bot.Messages;
import balik.englishbot.bot.command.AbstractCommand;
import balik.englishbot.bot.command.Commands;
import balik.englishbot.domain.User;
import balik.englishbot.util.RankMaker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class CurrentGameTurnCommand extends AbstractCommand {

    @Override
    protected void processUpdate(Update update, User user, SendMessage message) {
        if (user.getCurrentQuestion() != 0) {

            final String request = update.getMessage().getText();
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
                keyboardRow.add(new KeyboardButton(Commands.FINISH_GAME.getValue()));
                keyboard.add(keyboardRow);
            } else {
                String rank = RankMaker.determineRank(user.getScore(), dictionary.getSize());
                message.setText(message.getText() + rank);

                clearGame(user);
            }
        }
    }
}
