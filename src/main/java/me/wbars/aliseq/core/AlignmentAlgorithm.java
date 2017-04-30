package me.wbars.aliseq.core;

public interface AlignmentAlgorithm {
    Alignment align();

    long score(Alignment align);

    void stop();

    void setListener(AlignmentListener listener);
}
