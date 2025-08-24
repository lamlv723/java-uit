package utils;

import javax.swing.*;
import java.awt.*;

public class UIUtils {

    /**
     * Displays a scrollable and word-wrapped error message dialog.
     * @param parentComponent The parent component.
     * @param message The error message to display.
     * @param title The title of the dialog window.
     */
    public static void showErrorDialog(Component parentComponent, String message, String title) {
        // Create a JTextArea to hold the message
        JTextArea textArea = new JTextArea(message);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);

        // To make it look like a label rather than an editor
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));

        // Put the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set a preferred size for the scroll pane to control the dialog width
        scrollPane.setPreferredSize(new Dimension(400, 150));

        JOptionPane.showMessageDialog(parentComponent, scrollPane, title, JOptionPane.ERROR_MESSAGE);
    }
}