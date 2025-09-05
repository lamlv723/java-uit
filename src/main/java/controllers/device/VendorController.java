package controllers.device;

import models.main.Employee;
import services.device.VendorService;

import java.util.List;

import models.device.Vendor;

public class VendorController {
    private final VendorService vendorService;

    public VendorController() {
        this.vendorService = new VendorService();
    }

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public void addVendor(Vendor vendor, Employee currentUser) {
        vendorService.addVendor(vendor, currentUser);
    }

    public void updateVendor(Vendor vendor, Employee currentUser) {
        vendorService.updateVendor(vendor, currentUser);
    }

    public void deleteVendor(int vendorId, Employee currentUser) {
        vendorService.deleteVendor(vendorId, currentUser);
    }

    public Vendor getVendorById(int vendorId) {
        return vendorService.getVendorById(vendorId);
    }

    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }
}
