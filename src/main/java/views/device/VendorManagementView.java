package views.device;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.device.VendorController;
import services.device.VendorService;
import models.device.Vendor;
import views.device.components.VendorTable;

public class VendorManagementView extends JFrame {
    private VendorTable table;
    private VendorController vendorController;

    public VendorManagementView() {
        setTitle("Quản lý Nhà cung cấp");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        vendorController = new VendorController(new VendorService());
        table = new VendorTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        // Action for Add
        btnAdd.addActionListener(e -> {
            JTextField tfName = new JTextField();
            JTextField tfContact = new JTextField();
            JTextField tfPhone = new JTextField();
            JTextField tfEmail = new JTextField();
            JTextField tfAddress = new JTextField();
            Object[] message = {
                    "Tên nhà cung cấp:", tfName,
                    "Người liên hệ:", tfContact,
                    "Số điện thoại:", tfPhone,
                    "Email:", tfEmail,
                    "Địa chỉ:", tfAddress
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Nhà cung cấp",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String contact = tfContact.getText().trim();
                String phone = tfPhone.getText().trim();
                String email = tfEmail.getText().trim();
                String address = tfAddress.getText().trim();
                // Gọi service xử lý nghiệp vụ, trả về lỗi nếu có
                String error = vendorController.getVendorService().addVendorFromInput(name, contact, phone, email,
                        address, "ADMIN");
                if (error == null) {
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            Vendor vendor = vendorController.getVendorById(id);
            if (vendor == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField tfName = new JTextField(vendor.getVendorName());
            JTextField tfContact = new JTextField(vendor.getContactPerson());
            JTextField tfPhone = new JTextField(vendor.getPhoneNumber());
            JTextField tfEmail = new JTextField(vendor.getEmail());
            JTextField tfAddress = new JTextField(vendor.getAddress());
            Object[] message = {
                    "Tên nhà cung cấp:", tfName,
                    "Người liên hệ:", tfContact,
                    "Số điện thoại:", tfPhone,
                    "Email:", tfEmail,
                    "Địa chỉ:", tfAddress
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Nhà cung cấp", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String contact = tfContact.getText().trim();
                String phone = tfPhone.getText().trim();
                String email = tfEmail.getText().trim();
                String address = tfAddress.getText().trim();
                if (!name.isEmpty()) {
                    vendor.setVendorName(name);
                    vendor.setContactPerson(contact);
                    vendor.setPhoneNumber(phone);
                    vendor.setEmail(email);
                    vendor.setAddress(address);
                    vendorController.updateVendor(vendor, "ADMIN"); // TODO: lấy role thực tế nếu có
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                vendorController.deleteVendor(id, "ADMIN"); // TODO: lấy role thực tế nếu có
                loadDataToTable();
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<Vendor> list = vendorController.getAllVendors();
        table.setVendorData(list);
    }
}
