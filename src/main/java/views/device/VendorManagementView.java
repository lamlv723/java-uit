package views.device;

import dao.device.VendorDAOImpl;
import dao.device.interfaces.VendorDAO;
import models.device.Vendor;
import views.device.components.VendorTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class VendorManagementView extends JFrame {
    public VendorManagementView() {
        setTitle("Quản lý Nhà cung cấp");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        VendorTable table = new VendorTable();
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

    private void loadDataToTable(VendorTable table) {
        VendorDAO dao = new VendorDAOImpl();
        List<Vendor> list = dao.getAllVendors();
        table.setVendorData(list);
    }
}
