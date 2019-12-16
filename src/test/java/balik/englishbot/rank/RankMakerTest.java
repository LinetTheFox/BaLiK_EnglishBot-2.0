package balik.englishbot.rank;

import balik.englishbot.bot.Messages;
import balik.englishbot.util.RankMaker;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author BaLiK on 08.03.19
 **/
public class RankMakerTest {
    @Test
    public void testZero() {
        final int guess = 0;
        final int total = 0;
        final String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                + Messages.NO_RANK.getMessage();
        final String result = RankMaker.determineRank(guess, total);

        assertEquals(rankMessage, result);
    }

    @Test
    public void testRankA() {
        final int total = 100;
        for(int guess=100;guess>94;--guess){
            String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                    + Messages.RANK_A.getMessage();
            String result = RankMaker.determineRank(guess, total);

            assertEquals(rankMessage, result);
        }
    }

    @Test
    public void testRankB() {
        final int total = 100;
        for(int guess=94;guess>89;--guess){
            String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                    + Messages.RANK_B.getMessage();
            String result = RankMaker.determineRank(guess, total);

            assertEquals(rankMessage, result);
        }
    }

    @Test
    public void testRankC() {
        final int total = 100;
        for(int guess=89;guess>74;--guess){
            String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                    + Messages.RANK_C.getMessage();
            String result = RankMaker.determineRank(guess, total);

            assertEquals(rankMessage, result);
        }
    }

    @Test
    public void testRankD() {
        final int total = 100;
        for(int guess=74;guess>59;--guess){
            String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                    + Messages.RANK_D.getMessage();
            String result = RankMaker.determineRank(guess, total);

            assertEquals(rankMessage, result);
        }
    }

    @Test
    public void testRankE() {
        final int total = 100;
        for(int guess=59;guess>39;--guess){
            String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                    + Messages.RANK_E.getMessage();
            String result = RankMaker.determineRank(guess, total);

            assertEquals(rankMessage, result);
        }
    }

    @Test
    public void testRankF() {
        final int total = 100;
        for(int guess=39;guess>-1;--guess){
            String rankMessage = String.format(Messages.RANK.getMessage(), guess, total)
                    + Messages.RANK_F.getMessage();
            String result = RankMaker.determineRank(guess, total);

            assertEquals(rankMessage, result);
        }
    }
}