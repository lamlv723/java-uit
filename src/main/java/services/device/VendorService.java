package services.device;

import java.util.List;

import models.device.Vendor;

public interface VendorService {
    void addVendor(Vendor vendor, String currentUserRole);

    void updateVendor(Vendor vendor, String currentUserRole);

    void deleteVendor(int vendorId, String currentUserRole);

    Vendor getVendorById(int vendorId);

    List<Vendor> getAllVendors();
}
