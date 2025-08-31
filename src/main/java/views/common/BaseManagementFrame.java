package views.common;

import controllers.user.UserSession;
import ui.IconFactory;
import utils.UITheme;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

// Khung quản lý chung: header gradient, action bar CRUD, bảng dữ liệu.
@SuppressWarnings("serial")
public abstract class BaseManagementFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    protected JButton btnAdd, btnEdit, btnDelete;
    protected JTable table;
    protected JPanel actionBarPanel;
    private Point dragOffset;

    private final String headerTitle;
    private final String headerIconName;
    private final Color gradientStart;
    private final Color gradientEnd;

    protected BaseManagementFrame(String windowTitle,
            String headerTitle,
            String headerIconName,
            int width,
            int height,
            Color gradientStart,
            Color gradientEnd) {
        this.headerTitle = headerTitle;
        this.headerIconName = headerIconName;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;

        setTitle(windowTitle);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        try {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
        } catch (Throwable ignored) {
        }
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                } catch (Throwable ignored) {
                }
            }
        });
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_SOFT);

        JPanel header = buildHeader();
        JPanel actionBar = buildActionBar();
        this.actionBarPanel = actionBar;
        table = createTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(header);
        top.add(actionBar);
        getContentPane().add(top, BorderLayout.NORTH);

        JPanel centerCard = new JPanel(new BorderLayout());
        centerCard.setBackground(Color.WHITE);
        centerCard.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        centerCard.add(scroll, BorderLayout.CENTER);
        getContentPane().add(centerCard, BorderLayout.CENTER);

        customizeTable(table);
        applyRoles();
        initActionsInternal();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, gradientStart, getWidth(), 0, gradientEnd));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            public boolean isOpaque() {
                return false;
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        JLabel lblTitle = new JLabel(headerTitle);
        lblTitle.setFont(UITheme.fontBold(17));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblIcon = new JLabel(IconFactory.get(headerIconName, 20));
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleWrap.setOpaque(false);
        titleWrap.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleWrap.add(lblIcon);
        titleWrap.add(lblTitle);
        JButton close = new JButton(IconFactory.get("times", 16));
        close.setToolTipText("Đóng");
        close.setForeground(Color.WHITE);
        close.setBackground(new Color(0, 0, 0, 0));
        close.setOpaque(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());
        header.add(titleWrap, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);

        MouseAdapter dragger = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                if (dragOffset != null) {
                    Point p = e.getLocationOnScreen();
                    setLocation(p.x - dragOffset.x, p.y - dragOffset.y);
                }
            }
        };
        header.addMouseListener(dragger);
        header.addMouseMotionListener(dragger);
        titleWrap.addMouseListener(dragger);
        titleWrap.addMouseMotionListener(dragger);
        lblTitle.addMouseListener(dragger);
        lblTitle.addMouseMotionListener(dragger);
        lblIcon.addMouseListener(dragger);
        lblIcon.addMouseMotionListener(dragger);
        return header;
    }

    private JPanel buildActionBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        bar.setBackground(new Color(250, 250, 250));
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        // Gradient color spec (Add/Edit/Delete)
        btnAdd = createRoundedActionButton("Thêm",
                UITheme.PASTEL_GREEN_START, UITheme.PASTEL_GREEN_END, "plus");
        btnEdit = createRoundedActionButton("Sửa", UITheme.AMBER_START, UITheme.AMBER_END, "edit");
        btnDelete = createRoundedActionButton("Xóa", UITheme.SOFT_RED_START, UITheme.SOFT_RED_END, "trash");
        applyPrimaryHoverGradient(btnAdd, UITheme.PASTEL_GREEN_START, UITheme.PASTEL_GREEN_END);
        applyPrimaryHoverGradient(btnEdit, UITheme.AMBER_START, UITheme.AMBER_END);
        applyPrimaryHoverGradient(btnDelete, UITheme.SOFT_RED_START, UITheme.SOFT_RED_END);
        bar.add(btnAdd);
        bar.add(btnEdit);
        bar.add(btnDelete);
        return bar;
    }

    private JButton createRoundedActionButton(String text, Color startColor, Color endColor, String iconName) {
        final int radius = 6;
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color sc = (Color) getClientProperty("grad.start");
                Color ec = (Color) getClientProperty("grad.end");
                if (sc == null || ec == null) {
                    sc = startColor;
                    ec = endColor;
                }
                if (!isEnabled()) {
                    sc = sc.darker();
                    ec = ec.darker();
                }
                GradientPaint gp = new GradientPaint(0, 0, sc, getWidth(), 0, ec);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
                g2.dispose();
                super.paintComponent(g);
            }

            public void updateUI() {
                super.updateUI();
                setContentAreaFilled(false);
                setOpaque(false);
            }
        };
        b.putClientProperty("grad.start", startColor);
        b.putClientProperty("grad.end", endColor);
        b.setForeground(Color.WHITE);
        b.setFont(UITheme.fontMedium(14));
        b.setIcon(IconFactory.get(iconName, 16));
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setIconTextGap(6);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setFocusPainted(false);
        b.setMargin(new Insets(0, 0, 0, 0));
        return b;
    }

    private void applyPrimaryHoverGradient(JButton b, Color start, Color end) {
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) {
                    b.putClientProperty("grad.start", start.darker());
                    b.putClientProperty("grad.end", end.darker());
                    b.repaint();
                }
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) {
                    b.putClientProperty("grad.start", start);
                    b.putClientProperty("grad.end", end);
                    b.repaint();
                }
            }
        });
    }

    private void initActionsInternal() {
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            onEdit(row);
        });
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            onDelete(row);
        });
    }

    protected void applyRoles() {
        String role = UserSession.getInstance().getCurrentUserRole();
        boolean isAdmin = "Admin".equalsIgnoreCase(role);
        btnAdd.setVisible(isAdmin);
        btnEdit.setVisible(isAdmin);
        btnDelete.setVisible(isAdmin);
    }

    protected abstract JTable createTable();

    protected abstract void loadData();

    protected abstract void onAdd();

    protected abstract void onEdit(int selectedRow);

    protected abstract void onDelete(int selectedRow);

    protected void customizeTable(JTable table) { // default styling header
        JTableHeader h = table.getTableHeader();
        if (h != null) {
            h.setBackground(new Color(243, 244, 246));
            h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        }
        if (table.getRowHeight() < 26)
            table.setRowHeight(26);
    }
}
