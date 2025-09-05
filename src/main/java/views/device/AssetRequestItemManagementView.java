package views.device;

import controllers.device.AssetRequestItemController;
import controllers.user.UserSession;
import models.device.AssetRequestItem;
import models.main.Employee;
import utils.UIUtils;
import views.common.BaseManagementFrame;
import views.device.components.AssetRequestItemTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Quản lý Chi tiết Yêu cầu Tài sản - dùng BaseManagementFrame
 * Hỗ trợ mở ở chế độ tổng (tất cả) hoặc chỉ cho một request cụ thể.
 */
public class AssetRequestItemManagementView extends BaseManagementFrame {
    private final AssetRequestItemController assetRequestItemController;
    private final AssetRequestItemTable itemTable;
    private final Integer specificRequestId; // null => xem tất cả

    // Giữ instance cửa sổ tổng để refresh khi nơi khác tạo mới request items
    public static AssetRequestItemManagementView generalInstance = null;

    // Cửa sổ tổng
    public AssetRequestItemManagementView() {
        this(null);
        generalInstance = this;
    }

    // Cửa sổ chi tiết theo requestId
    public AssetRequestItemManagementView(Integer requestId) {
        super("Quản lý Chi tiết Yêu cầu Tài sản",
                (requestId == null ? "Chi tiết Yêu cầu Tài sản (Tất cả)" : "Chi tiết Yêu cầu Tài sản"),
                "clipboard-list", 950, 580,
                Color.decode("#1F1C2C"), Color.decode("#928DAB"));
        this.specificRequestId = requestId;
        assetRequestItemController = new AssetRequestItemController();
        itemTable = (AssetRequestItemTable) this.table;
        loadData();
        // Luôn ẩn các nút CRUD & action bar vì màn hình chỉ xem
        if (btnAdd != null)
            btnAdd.setVisible(false);
        if (btnEdit != null)
            btnEdit.setVisible(false);
        if (btnDelete != null)
            btnDelete.setVisible(false);
        if (actionBarPanel != null)
            actionBarPanel.setVisible(false);
        revalidate();
        repaint();
        if (requestId == null) {
            addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    if (generalInstance == AssetRequestItemManagementView.this)
                        generalInstance = null;
                }
            });
        }
    }

    public void refreshData() {
        loadData();
    }

    @Override
    protected JTable createTable() {
        // Nếu xem tất cả thì cần cột Request ID, nếu xem 1 request cụ thể thì bỏ
        return new AssetRequestItemTable(this.specificRequestId == null);
    }

    @Override
    protected void loadData() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        List<AssetRequestItem> all = assetRequestItemController.getFilteredRequestItems(currentUser);
        if (all == null) {
            UIUtils.showErrorDialog(this, "Không thể tải dữ liệu chi tiết yêu cầu.", "Lỗi");
            return;
        }
        if (specificRequestId != null) {
            List<AssetRequestItem> filtered = all.stream()
                    .filter(i -> i.getAssetRequest() != null
                            && specificRequestId.equals(i.getAssetRequest().getRequestId()))
                    .collect(Collectors.toList());
            itemTable.setAssetRequestItemData(filtered);
        } else {
            itemTable.setAssetRequestItemData(all);
        }
    }

    @Override
    protected void onAdd() {
        /* disabled */ }

    @Override
    protected void onEdit(int selectedRow) {
        /* disabled */ }

    @Override
    protected void onDelete(int selectedRow) {
        /* disabled */ }
}
