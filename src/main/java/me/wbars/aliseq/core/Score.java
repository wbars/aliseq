package me.wbars.aliseq.core;

public class Score {
    private final int gapsCount;
    private final int sameCharsCount;
    private final long score;

    public Score(int sameCharsCount, int gapsCount, long score) {
        this.gapsCount = gapsCount;
        this.sameCharsCount = sameCharsCount;
        this.score = score;
    }

    public int gapsCount() {
        return gapsCount;
    }

    public int sameCharsCount() {
        return sameCharsCount;
    }

    public long score() {
        return score;
    }


}
