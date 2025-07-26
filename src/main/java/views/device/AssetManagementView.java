package views.device;

import dao.device.AssetDAOImpl;
import dao.device.interfaces.AssetDAO;
import models.device.Asset;
import views.device.components.AssetTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class AssetManagementView extends JFrame {
    public AssetManagementView() {
        setTitle("Quản lý Tài sản");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        AssetTable table = new AssetTable();
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

    private void loadDataToTable(AssetTable table) {
        AssetDAO dao = new AssetDAOImpl();
        List<Asset> list = dao.getAll();
        table.setAssetData(list);
    }
}
