package views.device;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.device.VendorController;
import controllers.user.UserSession;
import services.device.VendorService;
import models.device.Vendor;
import views.device.components.VendorTable;

public class    VendorManagementView extends JFrame {
    private VendorTable table;
    private VendorController vendorController;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;

    public VendorManagementView() {
        setTitle("Quản lý Nhà cung cấp");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        vendorController = new VendorController(new VendorService());
        table = new VendorTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        applyRoles();

        // Action for Add
        btnAdd.addActionListener(e -> {
            String currentUserRole = UserSession.getInstance().getCurrentUserRole();
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
                try {
                    String error = vendorController.getVendorService().addVendorFromInput(name, contact, phone, email,
                            address, currentUserRole);
                    if (error == null) {
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi không mong muốn.", "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
            String currentUserRole = UserSession.getInstance().getCurrentUserRole();
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
                    try {
                        vendorController.updateVendor(vendor, currentUserRole);
                        loadDataToTable();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi không mong muốn.", "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            String currentUserRole = UserSession.getInstance().getCurrentUserRole();
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
                try {
                    vendorController.deleteVendor(id, currentUserRole);
                    loadDataToTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi không mong muốn.", "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<Vendor> list = vendorController.getAllVendors();
        table.setVendorData(list);
    }

    private void applyRoles() {
        String role = UserSession.getInstance().getCurrentUserRole();
        // Only Admin can manage vendors
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        btnAdd.setVisible(isAdmin);
        btnEdit.setVisible(isAdmin);
        btnDelete.setVisible(isAdmin);
    }
}
