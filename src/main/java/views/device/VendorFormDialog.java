package views.device;

import controllers.device.VendorController;
import models.device.Vendor;
import models.main.Employee;
import controllers.user.UserSession;
import views.common.BaseFormDialog;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog thêm / sửa Nhà cung cấp (header trắng, không nút X) dùng
 * BaseFormDialog.
 */
public class VendorFormDialog extends BaseFormDialog {
    private final VendorController vendorController;
    private final Vendor editing;

    private JTextField tfName;
    private JTextField tfContact;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextField tfAddress;

    public VendorFormDialog(Window owner, VendorController controller, Vendor vendor) {
        super(owner instanceof Frame ? (Frame) owner : null,
                480,
                420,
                "box",
                (vendor == null ? "Thêm Nhà cung cấp" : "Sửa Nhà cung cấp"));
        this.vendorController = controller;
        this.editing = vendor;
        buildUI();
    }

    @Override
    protected JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        // Tăng padding trái/phải để canh thẳng với header (header có left = 20)
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        tfName = new JTextField();
        tfContact = new JTextField();
        tfPhone = new JTextField();
        tfEmail = new JTextField();
        tfAddress = new JTextField();

        if (editing != null) {
            tfName.setText(editing.getVendorName());
            tfContact.setText(editing.getContactPerson());
            tfPhone.setText(editing.getPhoneNumber());
            tfEmail.setText(editing.getEmail());
            tfAddress.setText(editing.getAddress());
        }

        addLabeled(panel, gbc, 0, "Tên nhà cung cấp", tfName);
        addLabeled(panel, gbc, 1, "Người liên hệ", tfContact);
        addLabeled(panel, gbc, 2, "Số điện thoại", tfPhone);
        addLabeled(panel, gbc, 3, "Email", tfEmail);
        addLabeled(panel, gbc, 4, "Địa chỉ", tfAddress);

        return panel;
    }

    private void addLabeled(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    @Override
    protected void onSave() {
        String name = tfName.getText().trim();
        String contact = tfContact.getText().trim();
        String phone = tfPhone.getText().trim();
        String email = tfEmail.getText().trim();
        String address = tfAddress.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Employee user = UserSession.getInstance().getLoggedInEmployee();
        try {
            if (editing == null) {
                // create new vendor via service helper if exists else manual entity
                Vendor v = new Vendor();
                v.setVendorName(name);
                v.setContactPerson(contact);
                v.setPhoneNumber(phone);
                v.setEmail(email);
                v.setAddress(address);
                vendorController.addVendor(v, user);
            } else {
                editing.setVendorName(name);
                editing.setContactPerson(contact);
                editing.setPhoneNumber(phone);
                editing.setEmail(email);
                editing.setAddress(address);
                vendorController.updateVendor(editing, user);
            }
            saved = true;
            dispose();
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                    "Lỗi Hệ Thống");
            ex.printStackTrace();
        }
    }
}
