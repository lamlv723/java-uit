
package dao.device.interfaces;

import models.device.Vendor;
import java.util.List;

public interface VendorDAO {
    void addVendor(Vendor vendor);

    void updateVendor(Vendor vendor);

    void deleteVendor(int vendorId);

    Vendor getVendorById(int vendorId);

    List<Vendor> getAllVendors();
}
