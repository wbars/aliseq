package me.wbars.aliseq.core;

import me.wbars.aliseq.utils.Pair;

import static java.lang.Math.max;

public class NeedlemanWunschAlignmentAlgorithm implements AlignmentAlgorithm {

    private final Scoring scoring;

    public NeedlemanWunschAlignmentAlgorithm(Scoring scoring) {
        this.scoring = scoring;
    }

    @Override
    public Alignment align(String first, String second) {
        Pair<String, String> restore = restore(createPrefixesCostsMatrix(first, second), first, second);
        return new Alignment(restore.first(), restore.second());
    }

    private int[][] createPrefixesCostsMatrix(String first, String second) {
        int[][] dp = new int[first.length() + 1][second.length() + 1];
        for (int i = 0; i <= first.length(); i++) dp[i][0] = scoring.gapPenalty() * i;
        for (int i = 0; i <= second.length(); i++) dp[0][i] = scoring.gapPenalty() * i;

        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                int match = dp[i - 1][j - 1] + match(first.charAt(i - 1), second.charAt(j - 1));
                int delete = dp[i - 1][j] + scoring.gapPenalty();
                int insert = dp[i][j - 1] + scoring.gapPenalty();
                dp[i][j] = max(match, max(delete, insert));
            }
        }
        return dp;
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
