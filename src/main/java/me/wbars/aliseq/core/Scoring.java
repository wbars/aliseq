package me.wbars.aliseq.core;

import me.wbars.aliseq.utils.Pair;

public class Scoring {
    private final int gapPenalty;
    /**
     * InDel for Insert-Delete
     */
    private final int indelPenalty;
    private final int matchBonus;

    public static final Scoring DEFAULT = new Builder().build();

    private Scoring(int gapPenalty, int indelPenalty, int matchBonus) {
        this.gapPenalty = gapPenalty;
        this.indelPenalty = indelPenalty;
        this.matchBonus = matchBonus;
    }

    private Pair<Integer, Integer> sameCharsAndGaps(Alignment alignment) {
        int gapsCount = 0;
        int sameCharsCount = 0;
        for (int i = 0; i < alignment.length(); i++) {
            if (alignment.sameChars(i)) sameCharsCount++;
            else if (alignment.hasGap(i)) gapsCount++;
        }
        return new Pair<>(sameCharsCount, gapsCount);
    }

    public Score score(Alignment alignment) {
        Pair<Integer, Integer> charsAndGaps = sameCharsAndGaps(alignment);
        return new Score(
                charsAndGaps.first(),
                charsAndGaps.second(),
                score(alignment.first(), charsAndGaps.second(), charsAndGaps.first())
        );
    }

    private long score(String firstAlignment, long gapsCount, long sameCharsCount) {
        long differentChars = firstAlignment.length() - sameCharsCount - gapsCount;
        return -1 * (gapsCount * gapPenalty + differentChars * indelPenalty);
    }

    public int gapPenalty() {
        return gapPenalty;
    }

    public int indelPenalty() {
        return indelPenalty;
    }

    public int matchBonus() {
        return matchBonus;
    }


    public static class Builder {
        private int gapPenalty = -1;
        private int indelPenalty = -1;
        private int matchBonus = 1;

        public Builder gapPenalty(int gapPenalty) {
            this.gapPenalty = gapPenalty;
            return this;
        }

        public Builder indelPenalty(int indelPenalty) {
            this.indelPenalty = indelPenalty;
            return this;
        }

        public Builder matchBonus(int matchBonus) {
            this.matchBonus = matchBonus;
            return this;
        }

        public Scoring build() {
            return new Scoring(gapPenalty, indelPenalty, matchBonus);
        }
    }
}
