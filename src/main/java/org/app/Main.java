package org.app;

import org.app.gui.AssignmentTrackerGUI;

import javax.swing.*;

/**
 * Main entry point for BUP UCAM Assignment Tracker
 * A comprehensive assignment management system for Bangladesh University of Professionals
 * Now with modern Swing GUI interface
 */
public class Main {
    public static void main(String[] args) {
        // Set look and feel for better integration
        try {
            // Try to set Nimbus look and feel for modern appearance
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Use default look and feel if Nimbus is not available
        }

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new AssignmentTrackerGUI();
            } catch (Exception e) {
                System.err.println("An error occurred while starting the application:");
                System.err.println(e.getMessage());
                e.printStackTrace();

                // Show error dialog to user
                JOptionPane.showMessageDialog(null,
                    "Failed to start BUP UCAM Assignment Tracker:\n" + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}