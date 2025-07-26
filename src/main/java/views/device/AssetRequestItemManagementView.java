package views.device;

import dao.device.AssetRequestItemDAOImpl;
import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequestItem;
import views.device.components.AssetRequestItemTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class AssetRequestItemManagementView extends JFrame {
    public AssetRequestItemManagementView() {
        setTitle("Quản lý Chi tiết Yêu cầu Tài sản");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        AssetRequestItemTable table = new AssetRequestItemTable();
        loadDataToTable(table);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable(AssetRequestItemTable table) {
        AssetRequestItemDAO dao = new AssetRequestItemDAOImpl();
        List<AssetRequestItem> list = dao.getAllAssetRequestItems();
        table.setAssetRequestItemData(list);
    }
}
