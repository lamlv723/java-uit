package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// UI style helpers
public class UITheme {
    // Palette
    public static final Color PRIMARY_BLUE = new Color(0x3B82F6);
    public static final Color PRIMARY_BLUE_DARK = new Color(0x2563EB);
    public static final Color INDIGO = new Color(0x6366F1);
    public static final Color PURPLE = new Color(0x8B5CF6);
    public static final Color SUCCESS_GREEN = new Color(0x10B981);
    public static final Color WARNING_YELLOW = new Color(0xF59E0B);
    public static final Color ERROR_RED = new Color(0xEF4444);
    // Gradient ranges for CRUD buttons (updated darker palette)
    // Add: #34D399 → #059669
    public static final Color PASTEL_GREEN_START = new Color(0x34D399);
    public static final Color PASTEL_GREEN_END = new Color(0x059669);
    // Edit: #FBBF24 → #D97706
    public static final Color AMBER_START = new Color(0xFBBF24);
    public static final Color AMBER_END = new Color(0xD97706);
    // Delete: #F87171 → #DC2626
    public static final Color SOFT_RED_START = new Color(0xF87171);
    public static final Color SOFT_RED_END = new Color(0xDC2626);
    // Approve: #10B981 → #34D399
    public static final Color APPROVE_START = new Color(0x10B981);
    public static final Color APPROVE_END = new Color(0x34D399);
    // Reject: #7F1D1D → #E11D48
    public static final Color REJECT_START = new Color(0x7F1D1D);
    public static final Color REJECT_END = new Color(0xE11D48);
    // View Details: #2563EB → #3B82F6
    public static final Color DETAIL_START = new Color(0x2563EB);
    public static final Color DETAIL_END = new Color(0x3B82F6);
    public static final Color BG_SOFT = new Color(0xF9FAFB);
    public static final Color GRAY_100 = new Color(0xF3F4F6);
    public static final Color GRAY_200 = new Color(0xE5E7EB);
    public static final Color GRAY_300 = new Color(0xD1D5DB);
    public static final Color GRAY_500 = new Color(0x6B7280); // added for mid gray text
    public static final Color GRAY_600 = new Color(0x4B5563);
    public static final Color GRAY_700 = new Color(0x374151);

    public static Font fontRegular(float size) {
        return UIManager.getFont("Label.font").deriveFont(Font.PLAIN, size);
    }

    public static Font fontMedium(float size) {
        return UIManager.getFont("Label.font").deriveFont(Font.BOLD, size - 1f);
    }

    public static Font fontBold(float size) {
        return UIManager.getFont("Label.font").deriveFont(Font.BOLD, size);
    }

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY_BLUE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setFont(fontMedium(14));
        return b;
    }

    public static JButton coloredButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setFont(fontMedium(14));
        return b;
    }

    public static JPanel cardPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(bg);
        l.setForeground(fg);
        l.setFont(fontMedium(11));
        l.setBorder(new EmptyBorder(4, 8, 4, 8));
        return l;
    }
}
