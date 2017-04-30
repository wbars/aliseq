package me.wbars.aliseq.ui;

import me.wbars.aliseq.core.Alignment;
import me.wbars.aliseq.core.AlignmentAlgorithm;
import me.wbars.aliseq.core.AlignmentListener;

import javax.swing.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class AlignmentWorker extends SwingWorker<Alignment, Object> implements AlignmentListener {
    private final AlignmentAlgorithm alignmentAlgorithm;
    private final Consumer<Alignment> callback;

    public AlignmentWorker(AlignmentAlgorithm alignmentAlgorithm, Consumer<Alignment> callback) {
        this.alignmentAlgorithm = alignmentAlgorithm;
        this.callback = callback;
        this.alignmentAlgorithm.setListener(this);
    }

    @Override
    protected Alignment doInBackground() throws Exception {
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


    @Override
    public void consume(int progress) {
        setProgress(progress);
    }

    public void stop() {
        alignmentAlgorithm.stop();
        cancel(true);
    }
}
