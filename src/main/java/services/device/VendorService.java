
package services.device;

import java.util.List;
import dao.device.VendorDAOImpl;
import dao.device.interfaces.VendorDAO;
import models.device.Vendor;

public class VendorService {
    private VendorDAO vendorDAO;

    public VendorService() {
        this.vendorDAO = new VendorDAOImpl();
    }

    public void addVendor(Vendor vendor, String currentUserRole) {
        // TODO: Add role-based validation if needed
        vendorDAO.addVendor(vendor);
    }

    public void updateVendor(Vendor vendor, String currentUserRole) {
        // TODO: Add role-based validation if needed
        vendorDAO.updateVendor(vendor);
    }

    public void deleteVendor(int vendorId, String currentUserRole) {
        // TODO: Add role-based validation if needed
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
        try {
            addVendor(vendor, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm nhà cung cấp: " + ex.getMessage();
        }
        return null;
    }
}
