
package services.device;

import dao.device.AssetRequestItemDAOImpl;
import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequestItem;
import models.main.Employee;

import java.util.List;

public class AssetRequestItemService {
    private AssetRequestItemDAO assetRequestItemDAO;

    public AssetRequestItemService() {
        this.assetRequestItemDAO = new AssetRequestItemDAOImpl();
    }

    public void addAssetRequestItem(AssetRequestItem item, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestItemDAO.addAssetRequestItem(item);
    }

    public void updateAssetRequestItem(AssetRequestItem item, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestItemDAO.updateAssetRequestItem(item);
    }

    public void deleteAssetRequestItem(int id, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestItemDAO.deleteAssetRequestItem(id);
    }

    public AssetRequestItem getAssetRequestItemById(int id) {
        return assetRequestItemDAO.getAssetRequestItemById(id);
    }

    public List<AssetRequestItem> getAllAssetRequestItems() {
        return assetRequestItemDAO.getAllAssetRequestItems();
    }

    public List<AssetRequestItem> getAllBorrowedAssetRequestItems(Employee currentUser){
        return assetRequestItemDAO.getAllBorrowedAssetRequestItems(currentUser);
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm AssetRequestItem từ dữ liệu đầu vào
     * dạng String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addAssetRequestItemFromInput(String assetIdStr, String quantityStr, String currentUserRole) {
        int assetId, quantity;
        try {
            assetId = Integer.parseInt(assetIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception ex) {
            return "Dữ liệu không hợp lệ!";
        }
        AssetRequestItem item = new AssetRequestItem();
        item.setAssetId(assetId);
        item.setQuantity(quantity);
        try {
            addAssetRequestItem(item, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm chi tiết yêu cầu: " + ex.getMessage();
        }
        return null;
    }
}
