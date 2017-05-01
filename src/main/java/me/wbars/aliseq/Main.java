package me.wbars.aliseq;

import me.wbars.aliseq.ui.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        MainPanel mainPanel = new MainPanel();
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setSize(new Dimension(600, 480));
        mainFrame.setVisible(true);
        mainFrame.setJMenuBar(mainPanel.menuBar);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
