package me.wbars.aliseq.core;

public class AlignmentAlgorithmFactory {

    private AlignmentAlgorithmFactory() {
    }

    public static AlignmentAlgorithm createNWAlignment(Scoring scoring) {
        return new NeedlemanWunschAlignmentAlgorithm(scoring);
    }
}
