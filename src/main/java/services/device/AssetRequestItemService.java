
package services.device;

import dao.device.AssetRequestItemDAOImpl;
import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequestItem;
import models.main.Employee;

import java.util.List;

public class AssetRequestItemService {
    private final AssetRequestItemDAO assetRequestItemDAO;

    public AssetRequestItemService(AssetRequestItemDAO assetRequestItemDAO) {
        this.assetRequestItemDAO = assetRequestItemDAO;
    }

    // Backward-compatible default wiring
    public AssetRequestItemService() {
        this(new AssetRequestItemDAOImpl());
    }

    public void addAssetRequestItem(AssetRequestItem item, Employee currentUser) {
        assetRequestItemDAO.addAssetRequestItem(item);
    }

    public void updateAssetRequestItem(AssetRequestItem item, Employee currentUser) {
        assetRequestItemDAO.updateAssetRequestItem(item);
    }

    public void deleteAssetRequestItem(int id, Employee currentUser) {
        assetRequestItemDAO.deleteAssetRequestItem(id);
    }

    public AssetRequestItem getAssetRequestItemById(int id) {
        return assetRequestItemDAO.getAssetRequestItemById(id);
    }

    public List<AssetRequestItem> getAllAssetRequestItems() {
        return assetRequestItemDAO.getAllAssetRequestItems();
    }

    public List<AssetRequestItem> getFilteredRequestItems(Employee currentUser) {
        if (currentUser == null)
            return java.util.Collections.emptyList();

        // Order by requestItemId ASC
        java.util.Comparator<AssetRequestItem> byIdAsc = java.util.Comparator.comparing(
                AssetRequestItem::getRequestItemId,
                java.util.Comparator.nullsLast(Integer::compareTo));

        List<AssetRequestItem> all = assetRequestItemDAO.getAllAssetRequestItems();
        if (all == null)
            return java.util.Collections.emptyList();

        String role = currentUser.getRole();
        if ("Admin".equalsIgnoreCase(role)) {
            return all.stream()
                    .sorted(byIdAsc)
                    .collect(java.util.stream.Collectors.toList());
        }
        if ("Manager".equalsIgnoreCase(role)) {
            Integer deptId = currentUser.getDepartmentId();
            if (deptId == null)
                return java.util.Collections.emptyList();
            return all.stream()
                    .filter(i -> i.getAssetRequest() != null
                            && i.getAssetRequest().getEmployee() != null
                            && i.getAssetRequest().getEmployee().getDepartmentId() != null
                            && deptId.equals(i.getAssetRequest().getEmployee().getDepartmentId()))
                    .sorted(byIdAsc)
                    .collect(java.util.stream.Collectors.toList());
        }
        // Staff
        Integer empId = currentUser.getEmployeeId();
        if (empId == null)
            return java.util.Collections.emptyList();
        return all.stream()
                .filter(i -> i.getAssetRequest() != null
                        && i.getAssetRequest().getEmployee() != null
                        && empId.equals(i.getAssetRequest().getEmployee().getEmployeeId()))
                .sorted(byIdAsc)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm AssetRequestItem từ dữ liệu đầu vào
     * dạng String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addAssetRequestItemFromInput(String assetIdStr, String quantityStr, Employee currentUser) {
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
            addAssetRequestItem(item, currentUser);
        } catch (Exception ex) {
            return "Lỗi khi thêm chi tiết yêu cầu: " + ex.getMessage();
        }
        return null;
    }
}
