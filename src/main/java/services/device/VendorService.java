package services.device;

import java.util.List;
import java.util.ArrayList;
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
}
