package me.wbars.aliseq.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoringTest {
    @Test
    public void score() throws Exception {
        Score score = Scoring.DEFAULT.score(new Alignment("GCA-TGCU", "G-ATTACA"));
        assertThat(score.score(), is(4));
        assertThat(score.gapsCount(), is(2));
        assertThat(score.sameCharsCount(), is(4));
    }

    @Test
    public void customScoring() throws Exception {
        Scoring scoring = new Scoring.Builder()
                .gapPenalty(-2)
                .indelPenalty(-3)
                .matchBonus(5)
                .build();
        Alignment alignment = new Alignment("GCA-TGCU", "G-ATTACA");
        assertThat(scoring.score(alignment).score(), is(10));
    }
}