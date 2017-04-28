package me.wbars.aliseq.core;

public class Score {
    private final int gapsCount;
    private final int sameCharsCount;
    private final int score;

    public Score(int sameCharsCount, int gapsCount, int score) {
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

    public int score() {
        return score;
    }
}
