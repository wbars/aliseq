package me.wbars.aliseq.ui;

import me.wbars.aliseq.core.AlignmentAlgorithm;
import me.wbars.aliseq.core.AlignmentAlgorithmFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.format;

public class MainPanel extends JPanel {
    private JMenu menu = new JMenu("File");
    private JMenuItem menuExportAlignment = new JMenuItem("Export alignment");
    public final JMenuBar menuBar = new JMenuBar();

    private JCheckBox autoAlignCheckBox = new JCheckBox("Auto align");
    private JPanel autoAlignPanel = new JPanel();
    private JProgressBar progress = new JProgressBar();
    private JLabel score = createCenteredLabel(null);
    private JLabel firstAlignment = createCenteredLabel(null);
    private JLabel secondAlignment = createCenteredLabel(null);

    private JTextArea firstSeq = new JTextArea(3, 10);
    private JTextArea secondSeq = new JTextArea(3, 10);

    private AlignmentAlgorithm alignmentAlgorithm;
    private AlignmentWorker alignmentWorker;
    private JButton autoAlignButton = new JButton("Align");

    public MainPanel() {
        initMenu();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(largeRigid());
        add(new InputPanel());
        add(largeRigid());
        add(largeRigid());
        add(new OutputPanel());
        add(largeRigid());
    }

    private void initMenu() {
        menu.add(menuExportAlignment);
        menuBar.add(menu);

        menuExportAlignment.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setApproveButtonText("Save");
            int result = fileChooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;
            writeAlignmentsToFile(fileChooser.getSelectedFile() + ".txt");
        });
    }

    private void writeAlignmentsToFile(String pathname) {
        File fileName = new File(pathname);
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(fileName));
            outFile.write(alignmentInfo());
            outFile.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String alignmentInfo() {
        return String.format("First sequence: %n " +
                "%s %n " +
                "Second sequence: %n" +
                "%s %n%n" +
                "Alignment: %n" +
                "First: %n" +
                "%s %n" +
                "Second: %n" +
                "%s %n%n" +
                "%s", firstSeq.getText(), secondSeq.getText(), firstAlignment.getText(), secondAlignment.getText(), score.getText());
    }

    private Component largeRigid() {
        return Box.createRigidArea(new Dimension(0, 20));
    }

    private class OutputPanel extends JPanel {
        OutputPanel() {
            initAutoAlignPanel(autoAlignPanel);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            add(autoAlignPanel);
            add(smallRigid());
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

        private void initAutoAlignPanel(JPanel autoAlignPanel) {
            autoAlignPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            autoAlignPanel.add(autoAlignCheckBox);
            autoAlignPanel.add(autoAlignButton);
            autoAlignButton.setVisible(false);
            autoAlignCheckBox.setSelected(true);

            autoAlignCheckBox.addActionListener(it -> autoAlignButton.setVisible(!autoAlignCheckBox.isSelected()));
            autoAlignButton.addActionListener(it -> updateAlignmentText());
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
            if (autoAlignCheckBox.isSelected()) updateAlignmentText();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (autoAlignCheckBox.isSelected()) updateAlignmentText();
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
