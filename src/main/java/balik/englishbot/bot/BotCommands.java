package balik.englishbot.bot;

public enum BotCommands {
    START("/start"),
    HELP("HELP"),
    START_GAME("START GAME"),
    FINISH_GAME("FINISH GAME"),
    LIST("WORD LIST");

    private final String command;

    BotCommands(String message) {
        this.command = message;
    }

    public String getValue() {
        return command;
    }
}
