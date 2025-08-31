package views.components;

import javax.swing.*;
import java.awt.*;

/**
 * BadgeLabel: JLabel tuỳ biến hiển thị trạng thái tài sản với nền gradient & bo
 * góc.
 * Hỗ trợ các trạng thái: Available / Borrowed / Retired (fallback xám cho
 * khác).
 */
@SuppressWarnings("serial")
public class BadgeLabel extends JLabel {
    private static final long serialVersionUID = 1L;
    private final String status;

    public BadgeLabel(String status) {
        super(status, SwingConstants.CENTER); // căn giữa luôn
        this.status = status;
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        // Không để opaque để super.paintComponent không tô đè nền gradient tùy biến
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color start, end, textColor;
        String key = status == null ? "" : status.trim().toLowerCase();
        switch (key) {
            case "available":
                start = new Color(236, 253, 245);
                end = new Color(209, 250, 229);
                textColor = new Color(5, 150, 105);
                break;
            case "borrowed":
                start = new Color(255, 251, 235);
                end = new Color(254, 243, 199);
                textColor = new Color(202, 138, 4);
                break;
            case "retired":
                start = new Color(250, 245, 255);
                end = new Color(237, 233, 254);
                textColor = new Color(91, 33, 182);
                break;
            default:
                start = new Color(224, 231, 255);
                end = new Color(199, 210, 254);
                textColor = new Color(30, 41, 59);
        }

        // Nền gradient bo góc
        GradientPaint gp = new GradientPaint(0, 0, start, getWidth(), getHeight(), end);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();

        // Thiết lập màu chữ rồi để JLabel tự vẽ căn giữa chuẩn
        setForeground(textColor);
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        // đảm bảo chiều cao tối thiểu để không bị "nhảy lên đầu dòng"
        if (d.height < 22)
            d.height = 22;
        return d;
    }
}
