package views.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import models.device.AssetRequest;
import views.device.components.AssetRequestTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class AssetRequestManagementView extends JFrame {
    public AssetRequestManagementView() {
        setTitle("Quản lý Yêu cầu Tài sản");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        AssetRequestTable table = new AssetRequestTable();
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

    private void loadDataToTable(AssetRequestTable table) {
        AssetRequestDAO dao = new AssetRequestDAOImpl();
        List<AssetRequest> list = dao.getAllAssetRequests();
        table.setAssetRequestData(list);
    }
}
