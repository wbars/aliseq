package me.wbars.aliseq.core;

import me.wbars.aliseq.utils.Pair;

import java.math.BigInteger;

import static java.lang.Math.max;

public class NeedlemanWunschAlignmentAlgorithm implements AlignmentAlgorithm {

    private final Scoring scoring;
    private final String first;
    private final String second;
    private int[][] dp;
    private int i = 0;
    private int j = 0;
    private final BigInteger costsArea;
    private BigInteger proceedCells = BigInteger.ZERO;

    public NeedlemanWunschAlignmentAlgorithm(String first, String second, Scoring scoring) {
        this.scoring = scoring;
        this.first = first;
        this.second = second;
        this.dp = new int[first.length() + 1][second.length() + 1];
        costsArea = BigInteger.valueOf(dp.length).multiply(BigInteger.valueOf(dp[0].length));
    }

    @Override
    public Alignment align() {
        if (!isCostsFilled()) throw new IllegalStateException();

        Pair<String, String> restore = restore(dp, first, second);
        return new Alignment(restore.first(), restore.second());
    }

    @Override
    public long score(Alignment alignment) {
        return scoring.score(alignment).score();
    }

    @Override
    public boolean isCostsFilled() {
        return proceedCells.equals(costsArea);
    }

    @Override
    public void fillNextCost() {
        if (isCostsFilled()) throw new IllegalStateException();
        dp[i][j] = computeCost();
        advanceIterators();
        proceedCells = proceedCells.add(BigInteger.ONE);
    }

    private int computeCost() {
        if (i == 0) return scoring.gapPenalty() * j;
        if (j == 0) return scoring.gapPenalty() * i;

        int match = dp[i - 1][j - 1] + match(first.charAt(i - 1), second.charAt(j - 1));
        int delete = dp[i - 1][j] + scoring.gapPenalty();
        int insert = dp[i][j - 1] + scoring.gapPenalty();
        return max(match, max(delete, insert));
    }

    private void advanceIterators() {
        if (j < second.length()) j++;
        else {
            j = 0;
            i++;
        }
    }

    @Override
    public int progress() {
        return proceedCells.multiply(BigInteger.valueOf(100)).divide(costsArea).intValueExact();
    }

    @Override
    public void clear() {
        dp = null;
    }

    private Pair<String, String> restore(int[][] prefixesScores, String first, String second) {
        StringBuilder firstAlignment = new StringBuilder();
        StringBuilder secondAlignment = new StringBuilder();
        int i = first.length();
        int j = second.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && prefixesScores[i][j] == prefixesScores[i - 1][j - 1] + match(first.charAt(i - 1), second.charAt(j - 1))) {
                firstAlignment.append(first.charAt(i - 1));
                secondAlignment.append(second.charAt(j - 1));

                i--;
                j--;
            } else if (i > 0 && prefixesScores[i - 1][j] - 1 == prefixesScores[i][j]) {
                firstAlignment.append(first.charAt(i - 1));
                secondAlignment.append("-");

                i--;
            } else {
                firstAlignment.append("-");
                secondAlignment.append(second.charAt(j - 1));

                j--;
            }
        }
        return new Pair<>(firstAlignment.reverse().toString(), secondAlignment.reverse().toString());
    }

    private int match(char c, char c1) {
        return c == c1 ? scoring.matchBonus() : scoring.indelPenalty();
    }
}
