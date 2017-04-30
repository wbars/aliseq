package me.wbars.aliseq.ui;

import me.wbars.aliseq.core.AlignmentAlgorithm;
import me.wbars.aliseq.core.AlignmentAlgorithmFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static java.lang.String.format;

public class MyPanel extends JPanel {

    private JProgressBar progress = new JProgressBar();
    private JLabel score = createCenteredLabel(null);
    private JLabel firstAlignment = createCenteredLabel(null);
    private JLabel secondAlignment = createCenteredLabel(null);

    private JTextArea firstSeq = new JTextArea(3, 10);
    private JTextArea secondSeq = new JTextArea(3, 10);

    private AlignmentAlgorithm alignmentAlgorithm;
    private AlignmentWorker alignmentWorker;

    public MyPanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(largeRigid());
        add(new InputPanel());
        add(largeRigid());
        add(largeRigid());
        add(new OutputPanel());
        add(largeRigid());
    }

    private Component largeRigid() {
        return Box.createRigidArea(new Dimension(0, 20));
    }

    private class OutputPanel extends JPanel {
        OutputPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            add(createCenteredLabel("Alignment:"));
            add(smallRigid());
            add(progress);
            add(smallRigid());
            add(firstAlignment);
            add(smallRigid());
            add(secondAlignment);
            add(smallRigid());
            add(score);
        }
    }

    private class InputPanel extends JPanel {
        InputPanel() {
            firstSeq.getDocument().addDocumentListener(new UpdateAlignmentTextListener());
            secondSeq.getDocument().addDocumentListener(new UpdateAlignmentTextListener());

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(createCenteredLabel("First sequence:"));
            add(smallRigid());
            add(new JScrollPane(firstSeq));

            add(largeRigid());
            add(createCenteredLabel("Second sequence:"));
            add(smallRigid());
            add(new JScrollPane(secondSeq));
        }
    }

    private Component smallRigid() {
        return Box.createRigidArea(new Dimension(0, 5));
    }

    private JLabel createCenteredLabel(String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private class UpdateAlignmentTextListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateAlignmentText();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateAlignmentText();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    private void updateAlignmentText() {
        if (alignmentWorker != null && !alignmentWorker.isDone()) alignmentWorker.stop();
        alignmentAlgorithm = AlignmentAlgorithmFactory.createNWAlignment(trim(firstSeq.getText()), trim(secondSeq.getText()));
        alignmentWorker = new AlignmentWorker(alignmentAlgorithm, align -> {
            firstAlignment.setText(align.first());
            secondAlignment.setText(align.second());
            score.setText(format("Score: %d", alignmentAlgorithm.score(align)));
        });

        alignmentWorker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) progress.setValue((Integer) evt.getNewValue());
        });
        alignmentWorker.execute();

    }

    private String trim(String text) {
        return text;
    }
}
