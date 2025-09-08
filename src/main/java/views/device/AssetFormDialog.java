package views.device;

import controllers.device.AssetController;
import controllers.user.UserSession;
import models.device.Asset;
import models.device.AssetCategory;
import models.device.Vendor;
import models.main.Employee;
import controllers.device.AssetCategoryController;
import controllers.device.VendorController;
import ui.IconFactory;
import utils.UITheme;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
// (Gaussian blur imports removed after switching to layered shadow approach)

/**
 * Dialog thêm / chỉnh sửa Asset với custom header bo góc và box shadow.
 */
public class AssetFormDialog extends JDialog {
    private final AssetController assetController;
    private final Asset asset;

    private JTextField tfName;
    private JTextField tfSerial;
    private JComboBox<AssetCategory> cbCategory;
    private JComboBox<String> cbStatus;
    private JComboBox<Vendor> cbVendor;
    private JTextField tfPrice;
    private JTextField tfPurchaseDate;
    private JTextField tfWarrantyDate;
    private JTextArea taDesc;

    private boolean saved = false;
    private Point dragOffset;

    private final AssetCategoryController assetCategoryController;
    private final VendorController vendorController;

    public AssetFormDialog(Frame owner, AssetController assetController,
            AssetCategoryController assetCategoryController,
            VendorController vendorController, Asset asset) {
        super(owner, true);
        this.assetController = assetController;
        this.assetCategoryController = assetCategoryController;
        this.vendorController = vendorController;
        this.asset = asset;

        setUndecorated(true);
        setSize(520, 560);
        setLocationRelativeTo(owner);

        // Panel chính đơn giản với viền 1px, bỏ shadow
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        setContentPane(content);

        JPanel header = buildHeader();
        content.add(header, BorderLayout.NORTH);

        initForm();
        JPanel formPanel = buildFormPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        content.add(formPanel, BorderLayout.CENTER);

        JPanel btnBar = buildButtonBar();
        btnBar.setBorder(BorderFactory.createEmptyBorder(8, 20, 16, 20));
        content.add(btnBar, BorderLayout.SOUTH);
    }

    // Bỏ lớp shadow: không cần lớp con đặc biệt nữa

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(520, 48));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        JLabel lblIcon = new JLabel(IconFactory.get("laptop", 20));
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        JLabel lblTitle = new JLabel(asset == null ? "Thêm Tài sản Mới" : "Chỉnh sửa Tài sản");
        lblTitle.setFont(UITheme.fontBold(15));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        left.setOpaque(false);
        left.add(lblIcon);
        left.add(lblTitle);
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
        header.add(left, BorderLayout.WEST);
        return header;
    }

    private void initForm() {
        List<AssetCategory> categories = assetCategoryController.getAllAssetCategories();
        List<Vendor> vendors = vendorController.getAllVendors();

        tfName = new JTextField();
        tfSerial = new JTextField();
        cbCategory = new JComboBox<>(categories.toArray(new AssetCategory[0]));
        cbStatus = new JComboBox<>(new String[] { "Available", "Borrowed", "Retired" });
        cbVendor = new JComboBox<>();
        cbVendor.addItem(null);
        for (Vendor v : vendors)
            cbVendor.addItem(v);
        // Renderer hiển thị tên rõ ràng cho danh mục & nhà cung cấp
        cbCategory.setRenderer(createCategoryRenderer());
        cbVendor.setRenderer(createVendorRenderer());

        tfPrice = new JTextField();
        tfPurchaseDate = new JTextField();
        tfWarrantyDate = new JTextField();
        taDesc = new JTextArea(4, 20);
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);

        if (asset != null) {
            try {
                tfName.setText(asset.getAssetName());
            } catch (Throwable ignore) {
                try {
                    tfName.setText(asset.getName());
                } catch (Throwable ignored) {
                }
            }
            tfSerial.setText(asset.getSerialNumber());
            // Chọn danh mục theo ID để tránh khác instance
            if (asset.getCategory() != null) {
                Integer cid = asset.getCategory().getCategoryId();
                if (cid != null) {
                    for (int i = 0; i < cbCategory.getItemCount(); i++) {
                        AssetCategory c = cbCategory.getItemAt(i);
                        if (c != null && cid.equals(c.getCategoryId())) {
                            cbCategory.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
            // Trạng thái
            cbStatus.setSelectedItem(asset.getStatus());
            // Chọn vendor theo ID
            if (asset.getVendor() != null) {
                Integer vid = asset.getVendor().getVendorId();
                if (vid != null) {
                    for (int i = 0; i < cbVendor.getItemCount(); i++) {
                        Vendor v = cbVendor.getItemAt(i);
                        if (v != null && vid.equals(v.getVendorId())) {
                            cbVendor.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
            if (asset.getPurchasePrice() != null)
                tfPrice.setText(asset.getPurchasePrice().toString());
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            if (asset.getPurchaseDate() != null)
                tfPurchaseDate.setText(fmt.format(asset.getPurchaseDate()));
            if (asset.getWarrantyExpiryDate() != null)
                tfWarrantyDate.setText(fmt.format(asset.getWarrantyExpiryDate()));
            taDesc.setText(asset.getDescription());
        }
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addField(panel, gbc, "Tên tài sản *", tfName);
        addField(panel, gbc, "Serial number *", tfSerial);
        addField(panel, gbc, "Danh mục *", cbCategory);
        addField(panel, gbc, "Trạng thái *", cbStatus);
        addField(panel, gbc, "Nhà cung cấp", cbVendor);
        addField(panel, gbc, "Giá mua", tfPrice);
        addField(panel, gbc, "Ngày mua (dd/MM/yyyy)", tfPurchaseDate);
        addField(panel, gbc, "Hết bảo hành (dd/MM/yyyy)", tfWarrantyDate);

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
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 4));
        buttons.setOpaque(false);
        JButton btnSave = createButton("Lưu", UITheme.SUCCESS_GREEN);
        JButton btnCancel = createButton("Hủy", new Color(107, 114, 128));
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
        buttons.add(btnSave);
        buttons.add(btnCancel);
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
        UIUtils.showErrorDialog(this, "Tên tài sản không được rỗng", "Lỗi");
        return;
    }
    if (tfSerial.getText().trim().isEmpty()) {
        UIUtils.showErrorDialog(this, "Serial number không được rỗng", "Lỗi");
        return;
    }
    if (cbCategory.getSelectedItem() == null) {
        UIUtils.showErrorDialog(this, "Danh mục bắt buộc", "Lỗi");
        return;
    }
    String status = (String) cbStatus.getSelectedItem();
    if (status == null || status.isEmpty())
        status = "Available";

    Asset a = (asset == null ? new Asset() : asset);
    try {
        a.setAssetName(tfName.getText().trim());
    } catch (Throwable ignore) {
        try {
            a.setName(tfName.getText().trim());
        } catch (Throwable ignored) {
        }
    }
    a.setSerialNumber(tfSerial.getText().trim());
    a.setCategory((AssetCategory) cbCategory.getSelectedItem());
    a.setStatus(status);
    a.setVendor((Vendor) cbVendor.getSelectedItem());
    a.setDescription(taDesc.getText().trim());

    if (!tfPrice.getText().trim().isEmpty()) {
        try {
            a.setPurchasePrice(new BigDecimal(tfPrice.getText().trim()));
        } catch (NumberFormatException ex) {
            UIUtils.showErrorDialog(this, "Giá mua không hợp lệ", "Lỗi");
            return;
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setLenient(false);
    try {
        if (!tfPurchaseDate.getText().trim().isEmpty())
            a.setPurchaseDate(sdf.parse(tfPurchaseDate.getText().trim()));
        if (!tfWarrantyDate.getText().trim().isEmpty())
            a.setWarrantyExpiryDate(sdf.parse(tfWarrantyDate.getText().trim()));
    } catch (ParseException ex) {
        UIUtils.showErrorDialog(this, "Ngày không hợp lệ (dd/MM/yyyy)", "Lỗi");
        return;
    }

    Employee user = UserSession.getInstance().getLoggedInEmployee();
    try {
        if (asset == null)
            assetController.addAsset(a, user);
        else
            assetController.updateAsset(a, user);
        saved = true;
        dispose();
    } catch (IllegalStateException ex) {
        UIUtils.showErrorDialog(this, ex.getMessage(), "Lỗi");
    } catch (Exception ex) {
        UIUtils.showErrorDialog(this, "Không thể lưu tài sản: " + ex.getMessage(), "Lỗi Hệ thống");
        ex.printStackTrace();
    }
}

    public boolean isSaved() {
        return saved;
    }

    // === Renderers tùy chỉnh ===
    private ListCellRenderer<? super AssetCategory> createCategoryRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("— Chọn danh mục —");
                } else if (value instanceof AssetCategory) {
                    setText(extractCategoryName((AssetCategory) value));
                }
                return this;
            }
        };
    }

    private ListCellRenderer<? super Vendor> createVendorRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("— Không —");
                } else if (value instanceof Vendor) {
                    setText(extractVendorName((Vendor) value));
                }
                return this;
            }
        };
    }

    // Thử lấy tên danh mục với nhiều phương thức khác nhau để tránh lỗi khác
    // version model
    private String extractCategoryName(AssetCategory c) {
        if (c == null)
            return "";
        try {
            Object o = c.getClass().getMethod("getCategoryName").invoke(c);
            if (o != null)
                return o.toString();
        } catch (Exception ignored) {
        }
        try {
            Object o = c.getClass().getMethod("getName").invoke(c);
            if (o != null)
                return o.toString();
        } catch (Exception ignored) {
        }
        return c.toString();
    }

    private String extractVendorName(Vendor v) {
        if (v == null)
            return "";
        try {
            Object o = v.getClass().getMethod("getVendorName").invoke(v);
            if (o != null)
                return o.toString();
        } catch (Exception ignored) {
        }
        try {
            Object o = v.getClass().getMethod("getName").invoke(v);
            if (o != null)
                return o.toString();
        } catch (Exception ignored) {
        }
        return v.toString();
    }
}
