package balik.englishbot.handler;

import balik.englishbot.handler.impl.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class CommandHandler {
    private static Logger LOG = Logger.getLogger(CommandHandler.class);
    private static CommandHandler instance = null;
    private Map<String, AbstractCommand> commandMap;

    private CommandHandler() {
        commandMap = new HashMap<>();

        commandMap.put(Commands.START.getValue(), new StartCommand());
        commandMap.put(Commands.HELP.getValue(), new HelpCommand());
        commandMap.put(Commands.START_GAME.getValue(), new StartGameCommand());
        commandMap.put(Commands.FINISH_GAME.getValue(), new FinishGameCommand());
        commandMap.put(Commands.LIST.getValue(), new ListGameCommand());
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
