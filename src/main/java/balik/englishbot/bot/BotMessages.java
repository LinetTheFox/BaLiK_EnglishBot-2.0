package balik.englishbot.bot;

public enum BotMessages {
    START("Hello, <b>%s!</b>\n" +
            "This is the third installment of BaLiK_English_Bot updated and tweaked by Linet\n" +
            "Created mainly for myself, to train myself in Hungarian lexic, but can be reused\n" +
            "by anyone else in their language learning."),
    UNITE_CHOOSE("Choose the desired unit from the available"),
    TASK("<b>Translate</b>:\n%s"),
    CORRECT("Correct!\n\n"),
    TYPO("You have made couple typos, but the answer is pretty much correct. It's spelled as "), // add the correct spelling here
    WRONG(" Wrong!\nCorrect answer is <b>'%s'</b>\n\n"),
    RANK("You translated correctly <b>%d of %d words</b>\nYour rank is:\n"),
    SUBSCRIBE("<b>Subscribe to BaLiK's blog where he describes his way of becoming a programmer.</b>\n" +
            "https://t.me/balik_blog"),
    /**
     * RANKS
     **/
    NO_RANK("none"),
    RANK_A("Oh, looks like your name is Mark, buddy ;)"),
    RANK_B("Is that a native Hungarian I see?"),
    RANK_C("Not bad, but you need a bit more practice"),
    RANK_D("Practice some more and come back"),
    RANK_E("Mmmmm, kinda shitty, man"),
    RANK_F("Bruh, you must've used wrong language");

    private final String message;

    BotMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
