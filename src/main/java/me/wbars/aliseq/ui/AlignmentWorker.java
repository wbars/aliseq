package me.wbars.aliseq.ui;

import me.wbars.aliseq.core.Alignment;
import me.wbars.aliseq.core.AlignmentAlgorithm;

import javax.swing.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class AlignmentWorker extends SwingWorker<Alignment, Object> {
    private final AlignmentAlgorithm alignmentAlgorithm;
    private final Consumer<Alignment> callback;

    public AlignmentWorker(AlignmentAlgorithm alignmentAlgorithm, Consumer<Alignment> callback) {
        this.alignmentAlgorithm = alignmentAlgorithm;
        this.callback = callback;
    }

    @Override
    protected Alignment doInBackground() throws Exception {
        while (!alignmentAlgorithm.isCostsFilled() && !isCancelled()) {
            alignmentAlgorithm.fillNextCost();
            setProgress(alignmentAlgorithm.progress());
        }
        if (isCancelled()) {
            alignmentAlgorithm.clear();
            return Alignment.EMPTY;
        }
        return alignmentAlgorithm.align();
    }

    @Override
    protected void done() {
        try {
            if (!isCancelled()) callback.accept(get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
