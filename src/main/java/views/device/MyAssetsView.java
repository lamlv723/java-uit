package views.device;

import controllers.user.UserSession;
import models.device.Asset;
import models.main.Employee;
import controllers.device.AssetController;
import views.common.BaseManagementFrame;
import views.device.components.AssetTable;

import javax.swing.*;
import java.util.List;

/**
 * Màn hình "Tài sản của tôi" (read-only) tái sử dụng layout từ
 * BaseManagementFrame.
 * Ẩn các nút hành động Thêm/Sửa/Xóa và chỉ hiển thị danh sách tài sản đang
 * mượn.
 */
public class MyAssetsView extends BaseManagementFrame {
    private final AssetController assetController;
    private final AssetTable assetTable;

    public MyAssetsView() {
        super("Tài sản của tôi", "Tài sản của tôi", "laptop-white", 900, 520,
                java.awt.Color.decode("#2C3E50"), java.awt.Color.decode("#4CA1AF"));
        this.assetController = new AssetController();
        this.assetTable = (AssetTable) this.table; // lấy bảng do createTable cung cấp
        // Ẩn thanh action (read-only)
        if (this.actionBarPanel != null)
            this.actionBarPanel.setVisible(false);
        // Disable selection editing actions
        this.table.setEnabled(false);
        loadData();
    }

    @Override
    protected JTable createTable() {
        AssetTable t = new AssetTable();
        t.setEnabled(false);
        return t;
    }

    @Override
    protected void loadData() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        if (currentUser == null)
            return;
        List<Asset> myAssets = assetController.getBorrowedAssetsByEmployeeId(currentUser.getEmployeeId());
        assetTable.setAssetData(myAssets);
    }

    // Read-only: override action hooks to no-op
    @Override
    protected void onAdd() {
        /* no-op */ }

    @Override
    protected void onEdit(int selectedRow) {
        /* no-op */ }

    @Override
    protected void onDelete(int selectedRow) {
        /* no-op */ }
}