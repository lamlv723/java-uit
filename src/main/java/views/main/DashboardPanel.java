package views.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import controllers.device.AssetController;
import controllers.device.AssetRequestController;
import controllers.user.UserSession;
import dao.device.AssetRequestItemDAOImpl;
import models.device.Asset;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.main.Employee;
import services.device.AssetRequestService;
import services.device.AssetService;
import ui.IconName;
import utils.UITheme;
import views.device.AssetFormDialog;

/**
 * Dashboard panel tách riêng khỏi MainView.
 * Chứa: Quick actions, statistics, embedded Asset Management & Asset Request
 * Management.
 */
public class DashboardPanel extends JPanel {
    private final JLabel lblTotal;
    private final JLabel lblAvailable;
    private final JLabel lblInUse;
    private static final int CARD_RADIUS = 18; // thống nhất với stat card radius

    public DashboardPanel() {
        super(new BorderLayout());
        setBackground(new Color(239, 246, 255));

        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        // Uniform horizontal padding so title/subtitle align with quick action buttons
        scrollContent.setBorder(BorderFactory.createEmptyBorder(16, 24, 32, 24));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JLabel title = new JLabel("Dashboard");
        title.setFont(UITheme.fontBold(24));
        title.setForeground(UITheme.GRAY_700);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("Welcome to Asset Management System");
        sub.setFont(UITheme.fontRegular(14));
        sub.setForeground(UITheme.GRAY_600);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(sub);
        headerPanel.add(Box.createVerticalStrut(20));
        scrollContent.add(headerPanel);
        JPanel contentWrapper = new JPanel();
        contentWrapper.setOpaque(false);
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel quickPanel = new JPanel(new GridLayout(1, 4, 16, 16));
        quickPanel.setOpaque(false);
        quickPanel.add(quickCard("Create Request", IconName.PLUS, new Color(0xDBEAFE), new Color(0x93C5FD),
                () -> views.device.AssetRequestManagementView.showCreateDialogOnly()));
        quickPanel.add(quickCard("My Assets", IconName.BOXES, new Color(0xD1FAE5), new Color(0x6EE7B7),
                () -> new views.device.MyAssetsView().setVisible(true)));
        quickPanel.add(quickCard("Manage Assets", IconName.CLIPBOARD_LIST, new Color(0xE9D5FF), new Color(0xC4B5FD),
                () -> new views.device.AssetManagementView().setVisible(true)));
        quickPanel.add(quickCard("Employees", IconName.USERS, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
                () -> new views.main.EmployeeManagementView().setVisible(true)));
        contentWrapper.add(quickPanel);
        contentWrapper.add(Box.createVerticalStrut(28));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 16));
        statsPanel.setOpaque(false);
        lblTotal = statCard("Total Assets", IconName.BOX, new Color(0x3B82F6), new Color(0x2563EB));
        lblAvailable = statCard("Available", IconName.CHECK_CIRCLE, new Color(0x10B981), new Color(0x059669));
        lblInUse = statCard("In Use", IconName.USER_CLOCK, new Color(0xF59E0B), new Color(0xD97706));
        statsPanel.add(wrapStat(lblTotal));
        statsPanel.add(wrapStat(lblAvailable));
        statsPanel.add(wrapStat(lblInUse));
        contentWrapper.add(statsPanel);
        contentWrapper.add(Box.createVerticalStrut(24));

        // Asset Management Section
        JPanel assetSection = new RoundedSectionPanel();
        assetSection.setLayout(new BorderLayout());
        assetSection.setBorder(BorderFactory.createEmptyBorder(16, 16, 20, 16));
        JLabel assetSecTitle = new JLabel("Asset Management");
        assetSecTitle.setFont(UITheme.fontBold(18));
        assetSecTitle.setForeground(UITheme.GRAY_700);
        assetSection.add(assetSecTitle, BorderLayout.NORTH);
        DashboardAssetPanel dashboardAssetPanel = new DashboardAssetPanel();
        assetSection.add(dashboardAssetPanel, BorderLayout.CENTER);
        // Expose reload method via UIManager for external trigger
        UIManager.put("dashboard.reloadAssets", (Runnable) dashboardAssetPanel::reload);
        contentWrapper.add(assetSection);
        contentWrapper.add(Box.createVerticalStrut(32));

        // Asset Request Management Section
        JPanel requestSection = new RoundedSectionPanel();
        requestSection.setLayout(new BorderLayout());
        requestSection.setBorder(BorderFactory.createEmptyBorder(16, 16, 20, 16));
        JLabel reqTitle = new JLabel("Asset Request Management");
        reqTitle.setFont(UITheme.fontBold(18));
        reqTitle.setForeground(UITheme.GRAY_700);
        requestSection.add(reqTitle, BorderLayout.NORTH);
        DashboardRequestPanel dashboardRequestPanel = new DashboardRequestPanel();
        requestSection.add(dashboardRequestPanel, BorderLayout.CENTER);
        // Expose reload method via UIManager for external trigger
        UIManager.put("dashboard.reloadRequests", (Runnable) dashboardRequestPanel::reload);
        contentWrapper.add(requestSection);
        contentWrapper.add(Box.createVerticalStrut(32));

        scrollContent.add(contentWrapper);

        // Stats refresh runnable
        Runnable refreshStats = this::refreshStats;
        refreshStats.run();
        UIManager.put("dashboard.refreshStats", refreshStats);

        JScrollPane scroller = new JScrollPane(scrollContent);
        scroller.setBorder(null);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        add(scroller, BorderLayout.CENTER);
    }

    private void refreshStats() {
        AssetService as = new AssetService();
        lblTotal.setText(String.valueOf(as.countTotalAssets()));
        lblAvailable.setText(String.valueOf(as.countAvailableAssets()));
        lblInUse.setText(String.valueOf(as.countInUseAssets()));
    }

    private JPanel quickCard(String title, IconName iconName, Color iconBgStart, Color iconBgEnd, Runnable action) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
                g2.setColor(new Color(230, 230, 230));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public void updateUI() {
                super.updateUI();
                setOpaque(false);
            }
        };
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Icon wrapper
        JPanel iconWrap = new JPanel(new GridBagLayout());
        iconWrap.setOpaque(false);
        // Right margin between icon container and text
        iconWrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        int r = 12; // radius for icon container
        JComponent iconHolder = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (iconBgStart.getAlpha() == 0 && iconBgEnd.getAlpha() == 0) {
                    // transparent
                } else {
                    Paint paint;
                    if (iconBgEnd != null && !iconBgEnd.equals(iconBgStart)) {
                        paint = new GradientPaint(0, 0, iconBgStart, getWidth(), getHeight(), iconBgEnd);
                    } else {
                        paint = iconBgStart;
                    }
                    g2.setPaint(paint);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconHolder.setLayout(new GridBagLayout());
        iconHolder.setPreferredSize(new Dimension(48, 48));
        iconHolder.setMinimumSize(new Dimension(48, 48));
        iconHolder.setOpaque(false);
        JLabel icon = new JLabel(iconName.icon(28));
        iconHolder.add(icon);
        iconWrap.add(iconHolder);
        JLabel lbl = new JLabel(
                "<html><b>" + title + "</b><br><span style='font-size:11px;color:#555;'>Open</span></html>");
        lbl.setFont(UITheme.fontRegular(13));
        p.add(iconWrap, BorderLayout.WEST);
        p.add(lbl, BorderLayout.CENTER);
        p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                p.setBackground(new Color(245, 248, 255));
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                p.setBackground(Color.WHITE);
                repaint();
            }

            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
        return p;
    }

    // Panel bo góc cho các section lớn
    private final class RoundedSectionPanel extends JPanel {
        RoundedSectionPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JLabel statCard(String title, IconName iconName, Color startColor, Color endColor) {
        JLabel valueLabel = new JLabel("0", SwingConstants.LEFT);
        // Larger than title (title ~13px)
        valueLabel.setFont(UITheme.fontBold(30));
        valueLabel.setForeground(Color.WHITE);
        JPanel container = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Paint paint;
                if (endColor != null && !endColor.equals(startColor)) {
                    // Horizontal gradient (left -> right) per spec
                    paint = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
                } else {
                    paint = startColor;
                }
                g2.setPaint(paint);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g);
            }

            public void updateUI() {
                super.updateUI();
                setOpaque(false);
            }
        };
        container.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel t = new JLabel(title, SwingConstants.LEFT);
        // Make stat titles larger & bold than quick action titles (~13px)
        t.setFont(UITheme.fontBold(18));
        t.setForeground(Color.WHITE);
        JLabel ic = new JLabel(iconName.icon(40));
        ic.setForeground(Color.WHITE);
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(t, BorderLayout.WEST);
        top.add(ic, BorderLayout.EAST);
        container.add(top, BorderLayout.NORTH);
        // Bottom area with left-aligned value
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(valueLabel, BorderLayout.WEST);
        container.add(bottom, BorderLayout.SOUTH);
        // Store reference to container so we can retrieve later
        valueLabel.putClientProperty("stat.container", container);
        return valueLabel; // will be wrapped via client property
    }

    private JComponent wrapStat(JLabel valueLabel) {
        Object cp = valueLabel.getClientProperty("stat.container");
        if (cp instanceof JComponent)
            return (JComponent) cp;
        // Fallback: climb up two levels if valueLabel inside bottom panel
        Container p = valueLabel.getParent();
        if (p != null && p.getParent() instanceof JComponent)
            return (JComponent) p.getParent();
        return p instanceof JComponent ? (JComponent) p : valueLabel;
    }

    // ==== Embedded Request Panel ====
    private final class DashboardRequestPanel extends JPanel {
        private final JTable table;
        private final DefaultTableModel model;
        private final AssetRequestController controller;
        private final AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
        private final JButton btnAdd, btnEdit, btnDelete, btnApprove, btnReject, btnView;

        DashboardRequestPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);
            controller = new AssetRequestController(new AssetRequestService());
            // Tách CRUD trái + nút phụ phải
            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            leftPanel.setOpaque(false);
            btnAdd = smallActionButton("Add Request", UITheme.PASTEL_GREEN_START, UITheme.PASTEL_GREEN_END);
            btnEdit = smallActionButton("Edit", UITheme.AMBER_START, UITheme.AMBER_END);
            btnDelete = smallActionButton("Delete", UITheme.SOFT_RED_START, UITheme.SOFT_RED_END);
            leftPanel.add(btnAdd);
            leftPanel.add(Box.createHorizontalStrut(12));
            leftPanel.add(btnEdit);
            leftPanel.add(Box.createHorizontalStrut(12));
            leftPanel.add(btnDelete);
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            rightPanel.setOpaque(false);
            btnApprove = smallActionButton("Duyệt", UITheme.APPROVE_START, UITheme.APPROVE_END);
            btnReject = smallActionButton("Từ chối", UITheme.REJECT_START, UITheme.REJECT_END);
            btnView = smallActionButton("Chi tiết", UITheme.DETAIL_START, UITheme.DETAIL_END);
            rightPanel.add(btnApprove);
            rightPanel.add(Box.createHorizontalStrut(12));
            rightPanel.add(btnReject);
            rightPanel.add(Box.createHorizontalStrut(12));
            rightPanel.add(btnView);
            JPanel btnBar = new JPanel(new BorderLayout());
            btnBar.setOpaque(false);
            btnBar.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
            btnBar.add(leftPanel, BorderLayout.WEST);
            btnBar.add(rightPanel, BorderLayout.EAST);
            add(btnBar, BorderLayout.NORTH);
            model = new DefaultTableModel(
                    new Object[] { "ID", "Nhân viên", "Loại", "Ngày yêu cầu", "Trạng thái", "Tài sản" }, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            table = new JTable(model);
            table.setRowHeight(26);
            // Align ID column center
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            if (table.getColumnModel().getColumnCount() > 0) {
                table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            }
            add(new JScrollPane(table), BorderLayout.CENTER);
            applyRoleVisibility();
            wireActions();
            reload();
        }

        private JButton smallActionButton(String text, Color start, Color end) {
            JButton b = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color sc = (Color) getClientProperty("grad.start");
                    Color ec = (Color) getClientProperty("grad.end");
                    if (sc != null && ec != null) {
                        Paint paint = new GradientPaint(0, 0, sc, getWidth(), 0, ec);
                        g2.setPaint(paint);
                    } else {
                        g2.setColor(getBackground());
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS);
                    g2.dispose();
                    super.paintComponent(g);
                }

                @Override
                public void updateUI() {
                    super.updateUI();
                    setContentAreaFilled(false);
                    setFocusPainted(false);
                    setOpaque(false);
                }
            };
            b.setForeground(Color.WHITE);
            if (end == null) {
                b.setBackground(start);
            } else {
                b.putClientProperty("grad.start", start);
                b.putClientProperty("grad.end", end);
            }
            b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (end != null) {
                b.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        Color sc = ((Color) b.getClientProperty("grad.start")).darker();
                        Color ec = ((Color) b.getClientProperty("grad.end")).darker();
                        b.putClientProperty("grad.start", sc);
                        b.putClientProperty("grad.end", ec);
                        b.repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        b.putClientProperty("grad.start", start);
                        b.putClientProperty("grad.end", end);
                        b.repaint();
                    }
                });
            }
            return b;
        }

        private void applyRoleVisibility() {
            String role = UserSession.getInstance().getCurrentUserRole();
            boolean isAdmin = "Admin".equalsIgnoreCase(role);
            boolean isManager = "Manager".equalsIgnoreCase(role);
            boolean isStaff = "Staff".equalsIgnoreCase(role);
            btnAdd.setVisible(isAdmin || isManager || isStaff);
            btnEdit.setVisible(isAdmin || isManager);
            btnDelete.setVisible(isAdmin);
            btnApprove.setVisible(isAdmin || isManager);
            btnReject.setVisible(isAdmin || isManager);
        }

        private void wireActions() {
            btnAdd.addActionListener(e -> openCreateDialog());
            btnEdit.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                openEditDialog(r);
            });
            btnDelete.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                deleteSelected(r);
            });
            btnApprove.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                approveSelected(r);
            });
            btnReject.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                rejectSelected(r);
            });
            btnView.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                viewDetails(r);
            });
        }

        private void warnSelect() {
            JOptionPane.showMessageDialog(this, "Hãy chọn một yêu cầu trước!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }

        private void reload() {
            model.setRowCount(0);
            Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
            java.util.List<AssetRequest> list = controller.getAllAssetRequests(currentUser);
            if (list == null)
                return;
            for (AssetRequest r : list) {
                String type = r.getRequestType();
                if (type != null) {
                    if (type.equalsIgnoreCase("borrow"))
                        type = "Borrow";
                    else if (type.equalsIgnoreCase("return"))
                        type = "Return";
                }
                java.util.List<AssetRequestItem> items = itemDAO.getAssetRequestItemsByRequestId(r.getRequestId());
                String assets = items.stream().map(it -> it.getAsset() != null ? it.getAsset().getAssetName() : "")
                        .filter(s -> !s.isEmpty()).collect(java.util.stream.Collectors.joining(", "));
                model.addRow(
                        new Object[] { r.getRequestId(), r.getEmployee() != null ? r.getEmployee().getFullName() : "",
                                type, r.getRequestDate(), r.getStatus(), assets });
            }
        }

        private void openCreateDialog() {
            views.device.AssetRequestManagementView.showCreateDialogOnly();
        }

        private Integer rowId(int row) {
            return (Integer) model.getValueAt(row, 0);
        }

        private void openEditDialog(int row) {
            // Show only the edit dialog for the selected asset request, not the management
            // frame
            Integer id = rowId(row);
            views.device.AssetRequestManagementView.showEditDialogOnly(id);
        }

        private void deleteSelected(int row) {
            Integer id = rowId(row);
            int c = JOptionPane.showConfirmDialog(this, "Xóa yêu cầu #" + id + "?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION)
                return;
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            try {
                String err = controller.getAssetRequestService().deleteAssetRequest(id, user);
                if (err == null) {
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                    reload();
                    triggerStats();
                } else
                    JOptionPane.showMessageDialog(this, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void approveSelected(int row) {
            Integer id = rowId(row);
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            AssetRequest req = controller.getAssetRequestById(id);
            if (req == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String err;
            try {
                if ("borrow".equalsIgnoreCase(req.getRequestType()))
                    err = controller.getAssetRequestService().approveBorrowRequest(id, user);
                else
                    err = controller.getAssetRequestService().approveReturnRequest(id, user);
                if (err == null) {
                    JOptionPane.showMessageDialog(this, "Đã duyệt!");
                    reload();
                    triggerStats();
                } else
                    JOptionPane.showMessageDialog(this, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void rejectSelected(int row) {
            Integer id = rowId(row);
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            try {
                String err = controller.getAssetRequestService().rejectRequest(id, user);
                if (err == null) {
                    JOptionPane.showMessageDialog(this, "Đã từ chối!");
                    reload();
                } else
                    JOptionPane.showMessageDialog(this, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void viewDetails(int row) {
            Integer id = rowId(row);
            new views.device.AssetRequestItemManagementView(id).setVisible(true);
        }

        private void triggerStats() {
            Runnable r = (Runnable) UIManager.get("dashboard.refreshStats");
            if (r != null)
                SwingUtilities.invokeLater(r);
        }
    }

    // ==== Embedded Asset Panel ====
    private final class DashboardAssetPanel extends JPanel {
        private final JTable table;
        private final DefaultTableModel model;
        private final AssetController controller;
        private final dao.device.AssetRequestItemDAOImpl itemDAO = new dao.device.AssetRequestItemDAOImpl();
        private final JButton btnAdd, btnEdit, btnDelete;

        DashboardAssetPanel() {
            super(new BorderLayout(0, 12));
            setOpaque(false);
            controller = new AssetController(new AssetService());
            JPanel btnBar = new JPanel();
            btnBar.setLayout(new BoxLayout(btnBar, BoxLayout.X_AXIS));
            btnBar.setOpaque(false);
            btnBar.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
            btnAdd = smallActionButton("Add Asset", UITheme.PASTEL_GREEN_START, UITheme.PASTEL_GREEN_END);
            btnEdit = smallActionButton("Edit", UITheme.AMBER_START, UITheme.AMBER_END);
            btnDelete = smallActionButton("Delete", UITheme.SOFT_RED_START, UITheme.SOFT_RED_END);
            JButton[] buttons = { btnAdd, btnEdit, btnDelete };
            for (int i = 0; i < buttons.length; i++) {
                btnBar.add(buttons[i]);
                if (i < buttons.length - 1) {
                    btnBar.add(Box.createHorizontalStrut(10));
                }
            }
            add(btnBar, BorderLayout.NORTH);
            model = new DefaultTableModel(
                    new Object[] { "ID", "Tên tài sản", "Loại", "Trạng thái", "Người đang sử dụng" }, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            table = new JTable(model);
            table.setRowHeight(26);
            // Align ID column center
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            if (table.getColumnModel().getColumnCount() > 0) {
                table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            }
            add(new JScrollPane(table), BorderLayout.CENTER);
            applyRoleVisibility();
            wireActions();
            reload();
        }

        private JButton smallActionButton(String text, Color start, Color end) {
            JButton b = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color sc = (Color) getClientProperty("grad.start");
                    Color ec = (Color) getClientProperty("grad.end");
                    if (sc != null && ec != null) {
                        Paint paint = new GradientPaint(0, 0, sc, getWidth(), 0, ec);
                        g2.setPaint(paint);
                    } else {
                        g2.setColor(getBackground());
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS);
                    g2.dispose();
                    super.paintComponent(g);
                }

                @Override
                public void updateUI() {
                    super.updateUI();
                    setContentAreaFilled(false);
                    setFocusPainted(false);
                    setOpaque(false);
                }
            };
            b.setForeground(Color.WHITE);
            if (end == null) {
                b.setBackground(start);
            } else {
                b.putClientProperty("grad.start", start);
                b.putClientProperty("grad.end", end);
            }
            b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (end != null) {
                b.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        Color sc = ((Color) b.getClientProperty("grad.start")).darker();
                        Color ec = ((Color) b.getClientProperty("grad.end")).darker();
                        b.putClientProperty("grad.start", sc);
                        b.putClientProperty("grad.end", ec);
                        b.repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        b.putClientProperty("grad.start", start);
                        b.putClientProperty("grad.end", end);
                        b.repaint();
                    }
                });
            }
            return b;
        }

        private void applyRoleVisibility() {
            String role = UserSession.getInstance().getCurrentUserRole();
            boolean isAdmin = "Admin".equalsIgnoreCase(role);
            btnAdd.setVisible(isAdmin);
            btnEdit.setVisible(isAdmin);
            btnDelete.setVisible(isAdmin);
        }

        private void wireActions() {
            btnAdd.addActionListener(e -> openCreateDialog());
            btnEdit.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                openEditDialog(r);
            });
            btnDelete.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r == -1) {
                    warnSelect();
                    return;
                }
                deleteSelected(r);
            });
        }

        private void warnSelect() {
            JOptionPane.showMessageDialog(this, "Hãy chọn một tài sản trước!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }

        private Integer rowId(int row) {
            return (Integer) model.getValueAt(row, 0);
        }

        private void reload() {
            model.setRowCount(0);
            java.util.List<Asset> assets = controller.getAllAssets();
            if (assets == null)
                return;
            for (Asset a : assets) {
                if (a == null)
                    continue;
                Integer id = null;
                try {
                    id = (Integer) a.getClass().getMethod("getAssetId").invoke(a);
                } catch (Exception ignored) {
                }
                if (id == null)
                    continue;
                String name = null;
                try {
                    name = (String) a.getClass().getMethod("getAssetName").invoke(a);
                } catch (Exception ignored) {
                }
                if (name == null) {
                    try {
                        name = (String) a.getClass().getMethod("getName").invoke(a);
                    } catch (Exception ignored) {
                    }
                }
                String status = null;
                try {
                    status = (String) a.getClass().getMethod("getStatus").invoke(a);
                } catch (Exception ignored) {
                }
                if (status == null)
                    status = "";
                String displayStatus = (status.equalsIgnoreCase("Borrowed") || status.equalsIgnoreCase("In Use"))
                        ? "In Use"
                        : status;
                String categoryName = "";
                try {
                    Object cat = a.getClass().getMethod("getCategory").invoke(a);
                    if (cat != null) {
                        try {
                            Object cn = cat.getClass().getMethod("getCategoryName").invoke(cat);
                            if (cn != null)
                                categoryName = cn.toString();
                        } catch (Exception ignored) {
                        }
                        if (categoryName.isEmpty()) {
                            try {
                                Object cn = cat.getClass().getMethod("getName").invoke(cat);
                                if (cn != null)
                                    categoryName = cn.toString();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
                String currentUser = "";
                try {
                    dao.device.AssetRequestItemDAOImpl dao = itemDAO;
                    models.device.AssetRequestItem active = dao.findActiveBorrowItemByAssetId(id);
                    if (active != null && active.getAssetRequest() != null
                            && active.getAssetRequest().getEmployee() != null) {
                        currentUser = active.getAssetRequest().getEmployee().getFullName();
                    }
                } catch (Exception ex) {
                }
                model.addRow(new Object[] { id, name, categoryName, displayStatus, currentUser });
            }
        }

        private void openCreateDialog() {
            AssetFormDialog dlg = new AssetFormDialog(SwingUtilities.getWindowAncestor(this) instanceof Frame
                    ? (Frame) SwingUtilities.getWindowAncestor(this)
                    : null, controller, null);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                reload();
                triggerStats();
            }
        }

        private void openEditDialog(int row) {
            Integer id = rowId(row);
            Asset asset = controller.getAssetById(id);
            AssetFormDialog dlg = new AssetFormDialog(SwingUtilities.getWindowAncestor(this) instanceof Frame
                    ? (Frame) SwingUtilities.getWindowAncestor(this)
                    : null, controller, asset);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                reload();
                triggerStats();
            }
        }

        private void deleteSelected(int row) {
            Integer id = rowId(row);
            int c = JOptionPane.showConfirmDialog(this, "Xóa tài sản #" + id + "?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION)
                return;
            Asset asset = controller.getAssetById(id);
            try {
                controller.deleteAsset(asset, UserSession.getInstance().getLoggedInEmployee());
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                reload();
                triggerStats();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void triggerStats() {
            Runnable r = (Runnable) UIManager.get("dashboard.refreshStats");
            if (r != null)
                SwingUtilities.invokeLater(r);
        }
    }
}
