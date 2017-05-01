package me.wbars.aliseq.core;

import me.wbars.aliseq.utils.Pair;

import java.math.BigInteger;

import static java.lang.Math.max;

public class NeedlemanWunschAlignmentAlgorithm implements AlignmentAlgorithm {

    private final Scoring scoring;
    private final String first;
    private final String second;
    private int[][] dp;
    private final BigInteger costsArea;
    private BigInteger proceedCells = BigInteger.ZERO;
    private AlignmentListener listener;
    private volatile boolean stop;

    public NeedlemanWunschAlignmentAlgorithm(String first, String second, Scoring scoring) {
        this.scoring = scoring;
        this.first = removeSpaces(first);
        this.second = removeSpaces(second);
        this.dp = new int[first.length() + 1][second.length() + 1];
        costsArea = BigInteger.valueOf(dp.length).multiply(BigInteger.valueOf(dp[0].length));
    }

    private static String removeSpaces(String s) {
        return s.replaceAll("\\s+", "");
    }

    @Override
    public Alignment align() {
        fillPrefixesCostsMatrix();
        if (stop) return Alignment.EMPTY;

        Pair<String, String> restore = restore();
        return new Alignment(restore.first(), restore.second());
    }

    private void fillPrefixesCostsMatrix() {
        for (int i = 0; i <= first.length(); i++) {
            for (int j = 0; j <= second.length(); j++) {
                if (stop) {
                    dp = null;
                    return;
                }
                dp[i][j] = computeCost(i, j);
                proceedCells = proceedCells.add(BigInteger.ONE);
                if (listener != null) listener.consume(progress());
            }
        }
    }

    @Override
    public long score(Alignment alignment) {
        return scoring.score(alignment).score();
    }

    private int computeCost(int i, int j) {
        if (i == 0) return scoring.gapPenalty() * j;
        if (j == 0) return scoring.gapPenalty() * i;

        int match = dp[i - 1][j - 1] + match(first.charAt(i - 1), second.charAt(j - 1));
        int delete = dp[i - 1][j] + scoring.gapPenalty();
        int insert = dp[i][j - 1] + scoring.gapPenalty();
        return max(match, max(delete, insert));
    }

    private int progress() {
        return proceedCells.multiply(BigInteger.valueOf(100)).divide(costsArea).intValueExact();
    }

    @Override
    public void stop() {
        stop = true;
    }

    private Pair<String, String> restore() {
        StringBuilder firstAlignment = new StringBuilder();
        StringBuilder secondAlignment = new StringBuilder();
        int i = first.length();
        int j = second.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + match(first.charAt(i - 1), second.charAt(j - 1))) {
                firstAlignment.append(first.charAt(i - 1));
                secondAlignment.append(second.charAt(j - 1));

                i--;
                j--;
            } else if (i > 0 && dp[i - 1][j] - 1 == dp[i][j]) {
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

    @Override
    public void setListener(AlignmentListener listener) {
        this.listener = listener;
    }
}
