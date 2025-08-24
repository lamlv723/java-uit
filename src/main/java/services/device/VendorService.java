
package services.device;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.device.VendorDAOImpl;
import dao.device.interfaces.VendorDAO;
import models.device.Vendor;

public class VendorService {
    private VendorDAO vendorDAO;
    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

    public VendorService() {
        this.vendorDAO = new VendorDAOImpl();
    }

    public void addVendor(Vendor vendor, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to add a vendor.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        vendorDAO.addVendor(vendor);
    }

    public void updateVendor(Vendor vendor, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to update a vendor.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        vendorDAO.updateVendor(vendor);
    }

    public void deleteVendor(int vendorId, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to delete vendor with id " + vendorId + ".";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        vendorDAO.deleteVendor(vendorId);
    }

    public Vendor getVendorById(int vendorId) {
        return vendorDAO.getVendorById(vendorId);
    }

    public List<Vendor> getAllVendors() {
        return vendorDAO.getAllVendors();
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm vendor từ dữ liệu đầu vào dạng String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addVendorFromInput(String name, String contact, String phone, String email, String address,
            String currentUserRole) {
        if (name == null || name.isEmpty()) {
            return "Tên nhà cung cấp không được để trống!";
        }
        Vendor vendor = new Vendor();
        vendor.setVendorName(name);
        vendor.setContactPerson(contact);
        vendor.setPhoneNumber(phone);
        vendor.setEmail(email);
        vendor.setAddress(address);

        // Let error propagate and be caught in view layer
        addVendor(vendor, currentUserRole);

        return null;
    }
}
