package views.device;

import controllers.device.AssetController;
import controllers.device.AssetCategoryController;
import controllers.device.VendorController;
import controllers.user.UserSession;
import models.device.Asset;
import models.main.Employee;
import utils.UIUtils;
import views.common.BaseManagementFrame;
import views.components.BadgeLabel;
import views.device.components.AssetTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Tài sản - refactor dùng BaseManagementFrame
 */
public class AssetManagementView extends BaseManagementFrame {
    private final AssetController assetController;
    private final AssetTable assetTable;

    public AssetManagementView() {
        super("Tài sản", "Tài sản", "laptop-white", 1000, 700,
                Color.decode("#373B44"), Color.decode("#4286f4"));
        // Use no-arg controller (hides Service layer from View)
        assetController = new AssetController();
        assetTable = (AssetTable) this.table; // table do createTable cung cấp
        loadData(); // gọi sau khi controller sẵn sàng
    }

    @Override
    protected JTable createTable() {
        return new AssetTable();
    }

    @Override
    protected void loadData() {
        List<Asset> list = assetController.getAllAssets();
        if (list == null) {
            UIUtils.showErrorDialog(this, "Không thể tải dữ liệu tài sản.", "Lỗi");
            return;
        }
        assetTable.setAssetData(list);
        triggerDashboardRefresh();
        triggerDashboardAssetReload();
    }

    private void triggerDashboardAssetReload() {
        Runnable r = (Runnable) UIManager.get("dashboard.reloadAssets");
        if (r != null)
            SwingUtilities.invokeLater(r);
    }

    @Override
    protected void onAdd() {
        AssetFormDialog dialog = new AssetFormDialog(this,
                assetController,
                new AssetCategoryController(),
                new VendorController(),
                null);
        dialog.setVisible(true);
        loadData();
    }

    @Override
    protected void onEdit(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        Asset asset = assetController.getAssetById(id);
        if (asset == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        AssetFormDialog dialog = new AssetFormDialog(this,
                assetController,
                new AssetCategoryController(),
                new VendorController(),
                asset);
        dialog.setVisible(true);
        loadData();
    }

    @Override
    protected void onDelete(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        Asset asset = assetController.getAssetById(id);
        if (asset == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài sản này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
            try {
                assetController.deleteAsset(asset, currentUser);
                loadData();
            } catch (Exception ex) {
                UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void customizeTable(JTable table) {
        super.customizeTable(table);
        // Cột tình trạng (index 4) hiển thị badge
        if (table.getColumnModel().getColumnCount() > 4) {
            table.getColumnModel().getColumn(4).setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
                if (value == null)
                    return new JLabel("");
                BadgeLabel badge = new BadgeLabel(value.toString());
                badge.setHorizontalAlignment(SwingConstants.CENTER);
                badge.setVerticalAlignment(SwingConstants.CENTER);
                return badge;
            });
        }
        if (table.getRowHeight() < 28)
            table.setRowHeight(28);
    }

    private void triggerDashboardRefresh() {
        Runnable r = (Runnable) UIManager.get("dashboard.refreshStats");
        if (r != null)
            SwingUtilities.invokeLater(r);
    }
}
