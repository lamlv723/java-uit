package views.device;

import controllers.device.AssetCategoryController;
import controllers.user.UserSession;
import models.device.AssetCategory;
import models.main.Employee;
import ui.IconFactory;
import utils.UITheme;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Dialog thêm / chỉnh sửa AssetCategory với giao diện đồng bộ AssetFormDialog.
 */
@SuppressWarnings("serial")
public class AssetCategoryFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final AssetCategoryController controller;
    private final AssetCategory category; // null => add

    private JTextField tfName;
    private JTextArea taDesc;
    private boolean saved = false;
    private Point dragOffset;

    public AssetCategoryFormDialog(Frame owner, AssetCategoryController controller, AssetCategory category) {
        super(owner, true);
        this.controller = controller;
        this.category = category;

        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 360);
        setLocationRelativeTo(owner);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        setContentPane(content);

        content.add(buildHeader(), BorderLayout.NORTH);
        initForm();
        content.add(buildFormPanel(), BorderLayout.CENTER);
        content.add(buildButtonBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(480, 48));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        JLabel lblIcon = new JLabel(IconFactory.get("ruler-combined", 20));
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        JLabel lblTitle = new JLabel(category == null ? "Thêm Danh mục" : "Chỉnh sửa Danh mục");
        lblTitle.setFont(UITheme.fontBold(15));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        left.setOpaque(false);
        left.add(lblIcon);
        left.add(lblTitle);

        MouseAdapter dragger = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragOffset != null) {
                    Point p = e.getLocationOnScreen();
                    setLocation(p.x - dragOffset.x, p.y - dragOffset.y);
                }
            }
        };
        header.addMouseListener(dragger);
        header.addMouseMotionListener(dragger);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    private void initForm() {
        tfName = new JTextField();
        taDesc = new JTextArea(4, 20);
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);

        if (category != null) {
            tfName.setText(category.getCategoryName());
            taDesc.setText(category.getDescription());
        }
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addField(panel, gbc, "Tên danh mục *", tfName);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Mô tả"), gbc);
        gbc.gridy++;
        JScrollPane sp = new JScrollPane(taDesc);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(sp, gbc);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent input) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(input, gbc);
        gbc.gridy++;
    }

    private JPanel buildButtonBar() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        buttons.setOpaque(false);
        JButton btnSave = createButton("Lưu", UITheme.SUCCESS_GREEN);
        JButton btnCancel = createButton("Hủy", new Color(107, 114, 128));
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
        buttons.add(btnSave);
        buttons.add(btnCancel);
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 24, 16, 24));
        return buttons;
    }

    private JButton createButton(String text, Color color) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public void updateUI() {
                super.updateUI();
                setContentAreaFilled(false);
                setOpaque(false);
            }
        };
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(UITheme.fontMedium(14));
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void onSave() {
        if (tfName.getText().trim().isEmpty()) {
            UIUtils.showErrorDialog(this, "Tên danh mục không được rỗng", "Lỗi");
            return;
        }
        AssetCategory c = (category == null ? new AssetCategory() : category);
        String name = tfName.getText().trim();
        c.setCategoryName(name);
        c.setDescription(taDesc.getText().trim());

        Employee user = UserSession.getInstance().getLoggedInEmployee();
        try {
            if (category == null)
                controller.addAssetCategory(c, user);
            else
                controller.updateAssetCategory(c, user);
            saved = true;
            dispose();
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, "Không thể lưu danh mục: " + ex.getMessage(), "Lỗi Hệ thống");
            ex.printStackTrace();
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
