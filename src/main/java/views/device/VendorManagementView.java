package views.device;

import controllers.device.VendorController;
import controllers.user.UserSession;
import models.device.Vendor;
import models.main.Employee;
import utils.UIUtils;
import views.common.BaseManagementFrame;
import views.device.components.VendorTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Quản lý Nhà cung cấp - refactor dùng BaseManagementFrame & dialog chuẩn
 */
public class VendorManagementView extends BaseManagementFrame {
    private final VendorController vendorController;
    private final VendorTable vendorTable;

    public VendorManagementView() {
        super("Quản lý Nhà cung cấp", "Quản lý Nhà cung cấp", "box", 820, 560,
                Color.decode("#232526"), Color.decode("#414345"));
        vendorController = new VendorController();
        vendorTable = (VendorTable) this.table;
        loadData();
    }

    @Override
    protected JTable createTable() {
        return new VendorTable();
    }

    @Override
    protected void loadData() {
        List<Vendor> list = vendorController.getAllVendors();
        if (list == null) {
            UIUtils.showErrorDialog(this, "Không thể tải dữ liệu nhà cung cấp.", "Lỗi");
            return;
        }
        vendorTable.setVendorData(list);
    }

    @Override
    protected void onAdd() {
        VendorFormDialog d = new VendorFormDialog(this, vendorController, null);
        d.setVisible(true);
        if (d.isSaved())
            loadData();
    }

    @Override
    protected void onEdit(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        Vendor v = vendorController.getVendorById(id);
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        VendorFormDialog d = new VendorFormDialog(this, vendorController, v);
        d.setVisible(true);
        if (d.isSaved())
            loadData();
    }

    @Override
    protected void onDelete(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            try {
                vendorController.deleteVendor(id, user);
                loadData();
            } catch (Exception ex) {
                UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }
}
