package me.wbars.aliseq;

import me.wbars.aliseq.core.*;

import java.util.Scanner;

import static java.lang.String.format;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        AlignmentAlgorithm nwAlignment = AlignmentAlgorithmFactory.createNWAlignment(Scoring.DEFAULT);
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                out.print("First sequence: ");
                String first = in.nextLine();
                out.println();

                out.print("Second sequence: ");
                String second = in.nextLine();

                Alignment align = nwAlignment.align(first, second);
                out.println(align.toString());
                Score score = Scoring.DEFAULT.score(align);
                out.println(format("Score: %d", score.score()));
                out.println(format("Same chars: %d (%f)", score.sameCharsCount(), 1.0 * score.sameCharsCount() / align.length()));
                out.println(format("Gaps: %d (%f)", score.gapsCount(), 1.0 * score.gapsCount() / align.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
