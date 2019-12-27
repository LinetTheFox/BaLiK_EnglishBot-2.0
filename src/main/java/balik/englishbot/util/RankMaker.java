package balik.englishbot.util;

import balik.englishbot.bot.BotMessages;

public final class RankMaker {
    public static String determineRank(int guess, int total) {
        String rank = String.format(BotMessages.RANK.getMessage(), guess, total);
        if (total == 0) {
            return rank + BotMessages.NO_RANK.getMessage();
        }
        double percent = ((double) (guess * 100)) / total;
        if (percent >= 95.0d) {
            rank += BotMessages.RANK_A.getMessage();
        } else if (percent >= 90.0d && percent < 95.0d) {
            rank += BotMessages.RANK_B.getMessage();
        } else if (percent >= 75.0d && percent < 90.0d) {
            rank += BotMessages.RANK_C.getMessage();
        } else if (percent >= 60.0d && percent < 75.0d) {
            rank += BotMessages.RANK_D.getMessage();
        } else if (percent >= 40.0d && percent < 60.0d) {
            rank += BotMessages.RANK_E.getMessage();
        } else if (percent < 40.0d) {
            rank += BotMessages.RANK_F.getMessage();
        }

        return rank;
    }
}
