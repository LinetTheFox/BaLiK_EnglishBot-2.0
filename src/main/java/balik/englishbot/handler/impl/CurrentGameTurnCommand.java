package balik.englishbot.handler.impl;

import balik.englishbot.bot.BotMessages;
import balik.englishbot.handler.AbstractCommand;
import balik.englishbot.bot.BotCommands;
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

            int hammingDistance = hammingDistance(answer, request);
            if (hammingDistance == 0)  {
                message.setText(BotMessages.CORRECT.getMessage());
                user.setScore(user.getScore() + 1);
            } else if (hammingDistance < 3) {
                message.setText(BotMessages.TYPO.getMessage() + answer);
                user.setScore(user.getScore() + 1);
            } else {
                message.setText(String.format(BotMessages.WRONG.getMessage(), answer));
            }

            user.setCurrentQuestion(user.getCurrentQuestion() + 1);

            if (user.getCurrentQuestion() != dictionary.getSize() + 1) {
                message.setText(message.getText() + String.format(BotMessages.TASK.getMessage(),
                        dictionary.getKeyByIndex(user.getCurrentQuestion())));

                keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(BotCommands.FINISH_GAME.getValue()));
                keyboard.add(keyboardRow);
            } else {
                String rank = RankMaker.determineRank(user.getScore(), dictionary.getSize());
                message.setText(message.getText() + rank);

                clearGame(user);
            }
        }
    }

    private int hammingDistance(String correct, String answer) {
        int distance = 0;

        // Counts different chars at the same positions in both strings.
        for (int i = 0; i < Math.min(correct.length(), answer.length()); ++i) {
            if (correct.charAt(i) != answer.charAt(i)) {
                distance++;
            }
        }
        // If strings have different length, adds to the distance the difference
        distance += Math.abs(correct.length() - answer.length());

        return distance;
    }

}
