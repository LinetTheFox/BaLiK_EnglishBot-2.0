package balik.englishbot.bot;

public enum Messages {
    START("Hello, <b>%s!</b>\n" +
            "This bot is aimed at helping to memorize words for dictation-translation.\n" +
            "Do not judge strictly,it is on crutches and I am a monkey coder;)\n" +
            "I hope I'll help you."),
    UNITE_CHOOSE("Choose the desired unit from the available"),
    TASK("<b>Translate</b>:\n%s"),
    CORRECT("Correct!\n\n"),
    WRONG(" Wrong!\nCorrect answer is <b>'%s'</b>\n\n"),
    RANK("You translated correctly <b>%d of %d words</b>\nYour rank is:\n");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
