package balik.englishbot;

import balik.englishbot.bot.EnglishBot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Locale;

public class Starter {
    private static final Logger LOG = Logger.getLogger(Starter.class);

    public static void main(String[] args) {
        Locale.setDefault(new Locale("en"));
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new EnglishBot());
            LOG.info("Bot init done");
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
            LOG.error("Bot registration fail\n"+ e.getMessage());
        }
    }
}
