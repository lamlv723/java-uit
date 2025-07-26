package views.device;

import javax.swing.*;
import java.awt.*;
import views.device.components.AssetCategoryTable;
import dao.device.interfaces.AssetCategoryDAO;
import dao.device.AssetCategoryDAOImpl;
import models.device.AssetCategory;
import java.util.List;

public class AssetCategoryManagementView extends JFrame {
    private AssetCategoryTable table;
    private AssetCategoryDAO assetCategoryDAO;

    public AssetCategoryManagementView() {
        setTitle("Quản lý Danh mục Tài sản");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetCategoryDAO = new AssetCategoryDAOImpl();
        table = new AssetCategoryTable();
        loadDataToTable();
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

    private void loadDataToTable() {
        List<AssetCategory> list = assetCategoryDAO.getAllAssetCategories();
        table.setAssetCategoryData(list);
    }
}
