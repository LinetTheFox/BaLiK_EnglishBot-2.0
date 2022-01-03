package balik.englishbot.handler;

import balik.englishbot.bot.BotCommands;
import balik.englishbot.handler.impl.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CommandHandler {
    private static Logger LOG = LogManager.getLogger(CommandHandler.class);
    private static CommandHandler instance = null;
    private Map<String, AbstractCommand> commandMap;

    private CommandHandler() {
        commandMap = new HashMap<>();

        commandMap.put(BotCommands.START.getValue(), new StartCommand());
        commandMap.put(BotCommands.HELP.getValue(), new HelpCommand());
        commandMap.put(BotCommands.START_GAME.getValue(), new StartGameCommand());
        commandMap.put(BotCommands.FINISH_GAME.getValue(), new FinishGameCommand());
        commandMap.put(BotCommands.LIST.getValue(), new ListGameCommand());
        commandMap.put("default", new CurrentGameTurnCommand());
    }

    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
            LOG.info("CommandHandler instance created.");
        }
        return instance;
    }


    public AbstractCommand getCommand(String command) {
        return commandMap.containsKey(command) ? commandMap.get(command) : commandMap.get("default");
    }
}
