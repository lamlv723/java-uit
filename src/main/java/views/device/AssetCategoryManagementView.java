
package views.device;

import controllers.device.AssetCategoryController;
import controllers.user.UserSession;
import models.device.AssetCategory;
import models.main.Employee;
import services.device.AssetCategoryService;
import utils.UIUtils;
import views.device.components.AssetCategoryTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Quản lý Danh mục Tài sản với UI đồng bộ AssetManagementView.
 */
@SuppressWarnings("serial")
public class AssetCategoryManagementView extends views.common.BaseManagementFrame {
    private static final long serialVersionUID = 1L;
    private final AssetCategoryController assetCategoryController;
    private final AssetCategoryTable assetCategoryTable;

    public AssetCategoryManagementView() {
        super("Quản lý Danh mục Tài sản", "Quản lý Danh mục Tài sản", "ruler-combined", 800, 550,
                Color.decode("#1E3C72"), Color.decode("#2A5298"));
        assetCategoryController = new AssetCategoryController(new AssetCategoryService());
        assetCategoryTable = (AssetCategoryTable) this.table; // cast an toàn vì createTable trả về loại này
        loadData(); // gọi sau khi controller đã sẵn sàng
    }

    @Override
    protected JTable createTable() {
        return new AssetCategoryTable();
    }

    @Override
    protected void loadData() {
        List<AssetCategory> list = assetCategoryController.getAllAssetCategories();
        if (list == null) {
            UIUtils.showErrorDialog(this, "Không thể tải dữ liệu danh mục.", "Lỗi");
            return;
        }
        assetCategoryTable.setAssetCategoryData(list);
    }

    @Override
    protected void onAdd() {
        AssetCategoryFormDialog dialog = new AssetCategoryFormDialog(this, assetCategoryController, null);
        dialog.setVisible(true);
        if (dialog.isSaved())
            loadData();
    }

    @Override
    protected void onEdit(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        AssetCategory cat = assetCategoryController.getAssetCategoryById(id);
        if (cat == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        AssetCategoryFormDialog dialog = new AssetCategoryFormDialog(this, assetCategoryController, cat);
        dialog.setVisible(true);
        if (dialog.isSaved())
            loadData();
    }

    @Override
    protected void onDelete(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa danh mục này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            try {
                assetCategoryController.deleteAssetCategory(id, user);
                loadData();
            } catch (Exception ex) {
                UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }
}
