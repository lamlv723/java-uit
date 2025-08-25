package views.device;

import controllers.device.AssetRequestItemController;
import services.device.AssetRequestItemService;
import controllers.user.UserSession;
import models.main.Employee;
import models.device.AssetRequestItem;
import views.device.components.AssetRequestItemTable;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import java.awt.*;

public class AssetRequestItemManagementView extends JFrame {
    private final AssetRequestItemController assetRequestItemController;
    private final AssetRequestItemTable table;
    private final Integer specificRequestId; // Dùng để xác định đây là view chi tiết hay view tổng

    /**
     * Biến static này sẽ giữ tham chiếu đến cửa sổ "Xem tất cả chi tiết".
     * Khi một request mới được tạo ở cửa sổ khác, nó sẽ gọi đến biến này để làm mới dữ liệu.
     */
    public static AssetRequestItemManagementView generalInstance = null;

    /**
     * Constructor cho cửa sổ xem TẤT CẢ items (QLCTYCTS tổng).
     * Được gọi từ menu chính.
     */
    public AssetRequestItemManagementView() {
        this(null);
        generalInstance = this;
        setTitle("Quản lý Chi tiết Yêu cầu Tài sản (Tất cả)");

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                generalInstance = null;
            }
        });
    }

    public AssetRequestItemManagementView(Integer requestId) {
        this.specificRequestId = requestId;
        setTitle("Quản lý Chi tiết Yêu cầu Tài sản");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetRequestItemController = new AssetRequestItemController(new AssetRequestItemService());
        table = new AssetRequestItemTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        loadDataToTable();
    }

    private void loadDataToTable() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        List<AssetRequestItem> allItems = assetRequestItemController.getFilteredRequestItems(currentUser);
        
        if (specificRequestId != null) {
            // Nếu đây là cửa sổ chi tiết, lọc danh sách chỉ hiển thị các item của request này
            List<AssetRequestItem> filteredList = allItems.stream()
                .filter(item -> specificRequestId.equals(item.getAssetRequest().getRequestId()))
                .collect(Collectors.toList());
            table.setAssetRequestItemData(filteredList);
        } else {
            // Nếu là cửa sổ tổng, hiển thị tất cả
            table.setAssetRequestItemData(allItems);
        }
    }
}
