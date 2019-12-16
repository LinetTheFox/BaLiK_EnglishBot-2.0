package balik.englishbot.bot.command.impl;

import balik.englishbot.bot.command.AbstractCommand;
import balik.englishbot.domain.User;
import balik.englishbot.util.RankMaker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FinishGameCommand extends AbstractCommand {

    @Override
    protected void processUpdate(Update update, User user, SendMessage message) {
        if (user.getCurrentQuestion() == 0) {
            return;
        }

        String playerRank = RankMaker.determineRank(user.getScore(), user.getCurrentQuestion() - 1);
        message.setText(playerRank);

        clearGame(user);
    }
}
