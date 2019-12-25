package balik.englishbot.handler.impl;

import balik.englishbot.bot.Messages;
import balik.englishbot.handler.AbstractCommand;
import balik.englishbot.domain.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand extends AbstractCommand {

    @Override
    protected void processUpdate(Update update, User user, SendMessage message) {
        if (user.getName() == null) {
            message.setText(String.format(Messages.START.getMessage(), "noname"));
        } else {
            message.setText(String.format(Messages.START.getMessage(), user.getName()));
        }

        clearGame(user);
    }
}
