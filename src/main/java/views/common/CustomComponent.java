package views.common;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;

public class CustomComponent {

    // ----- Button -----
    public static class PrimaryButton extends JButton {
        public PrimaryButton(String text) {
            super(text);
            putClientProperty("JButton.buttonType", "roundRect");
            putClientProperty("JButton.defaultButton", true);
        }
    }

    // ----- TextField -----
    public static class SearchField extends JTextField {
        public SearchField(String placeholder) {
            super();
            putClientProperty("JTextField.placeholderText", placeholder);
            putClientProperty("JComponent.roundRect", true);
        }
    }

    public static class PasswordFieldWithReveal extends JPasswordField {
        public PasswordFieldWithReveal(String placeholder) {
            super();
            putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
            putClientProperty("JPasswordField.showRevealButton", true);
            putClientProperty("JComponent.roundRect", true);
        }
    }

    // ----- Table -----
    public static class BaseTable extends JTable {
        public BaseTable() {
            super();
            setFillsViewportHeight(true);
            setRowHeight(36);
            setShowHorizontalLines(true);
            setShowVerticalLines(false);
            putClientProperty("JTable.striped", true);
        }
    }
}
