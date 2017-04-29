package me.wbars.aliseq.core;

public interface AlignmentAlgorithm {
    Alignment align();

    long score(Alignment align);

    boolean isCostsFilled();

    void fillNextCost();

    int progress();

    void clear();
}
