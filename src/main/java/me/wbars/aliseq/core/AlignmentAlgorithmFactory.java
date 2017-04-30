package me.wbars.aliseq.core;

public class AlignmentAlgorithmFactory {

    private AlignmentAlgorithmFactory() {
    }

    public static NeedlemanWunschAlignmentAlgorithm createNWAlignment(String first, String second) {
        return new NeedlemanWunschAlignmentAlgorithm(first, second, Scoring.DEFAULT);
    }
}
