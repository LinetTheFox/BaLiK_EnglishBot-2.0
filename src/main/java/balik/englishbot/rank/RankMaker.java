package balik.englishbot.rank;

import balik.englishbot.bot.Messages;

public class RankMaker {
    public static String determineRank(int guess, int total) {
        double percent = ((double) (guess * 100)) / total;
        String rank = String.format(Messages.RANK.getMessage(), guess, total);
        if (percent >= 95.0d)
            rank += " Вас случайно не Валентин зовут?)))0)";
        else if (percent >= 90.0d && percent < 95.0d)
            rank += "О,сэр, да вы из Англии!";
        else if (percent >= 75.0d && percent < 90.0d)
            rank += "Not bad, потренируйся еще";
        else if (percent >= 60.0d && percent < 75.0d)
            rank += "Подучи и приходи еще";
        else if (percent >= 40.0d && percent < 60.0d)
            rank += "London is the capital of Great Britain";
        else if (percent < 40.0d)
            rank += "Брат, не на тот язык переводил:(";

        return rank;
    }
}
