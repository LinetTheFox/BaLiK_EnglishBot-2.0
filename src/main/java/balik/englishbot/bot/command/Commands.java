package balik.englishbot.bot.command;

public enum Commands {
    START("/start"),
    HELP("HELP"),
    START_GAME("START GAME"),
    FINISH_GAME("FINISH GAME"),
    LIST("WORD LIST");

    private final String command;

    Commands(String message) {
        this.command = message;
    }

    public String getValue() {
        return command;
    }
}
