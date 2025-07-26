package controllers.device;

import services.device.VendorService;

import java.util.List;

import models.device.Vendor;

public class VendorController {
    private VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public void addVendor(Vendor vendor, String currentUserRole) {
        vendorService.addVendor(vendor, currentUserRole);
    }

    public void updateVendor(Vendor vendor, String currentUserRole) {
        vendorService.updateVendor(vendor, currentUserRole);
    }

    public void deleteVendor(int vendorId, String currentUserRole) {
        vendorService.deleteVendor(vendorId, currentUserRole);
    }

    public Vendor getVendorById(int vendorId) {
        return vendorService.getVendorById(vendorId);
    }

    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }
}
