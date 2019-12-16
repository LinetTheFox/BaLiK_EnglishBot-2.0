package balik.englishbot.bot.command.impl;

import balik.englishbot.bot.command.AbstractCommand;
import balik.englishbot.domain.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand extends AbstractCommand {
    @Override
    protected void processUpdate(Update update, User user, SendMessage message) {
        //todo: refactor
    }
}
