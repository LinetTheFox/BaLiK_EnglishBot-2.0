package balik.englishbot.handler;

import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.Assert.*;

public class CommandHandlerTest {
    private static final CommandHandler COMMAND_HANDLER = CommandHandler.getInstance();
    private static final String START_COMMAND = "/start";
    private static final String START_MESSAGE = "Hello, <b>%s!</b>\n" +
            "This bot is aimed at helping to memorize words for dictation-translation.\n" +
            "Do not judge strictly,it is on crutches and I am a monkey coder;)\n" +
            "I hope I'll help you.";

    // @Test
    // public void testStartMessage() {
    //     SendMessage message = COMMAND_HANDLER.getCommand(START_COMMAND).handleMessage(new Update(), null);
    //     assertEquals(START_MESSAGE, message.getText());
    // }
}