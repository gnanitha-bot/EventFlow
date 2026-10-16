package main;

import ui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Main Entry Point for EventFlow application.
 */
public class Main {
    public static void main(String[] args) {
        // Enable anti-aliased font rendering for smooth UI text
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                // Set Native System Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fallback to default Swing Look & Feel silently if system L&F unavailable
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
