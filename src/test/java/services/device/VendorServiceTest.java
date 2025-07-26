package services.device;

import dao.device.interfaces.VendorDAO;
import models.device.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendorServiceTest {
    private VendorService vendorService;
    private VendorDAO vendorDAOMock;

    @BeforeEach
    void setUp() {
        vendorDAOMock = Mockito.mock(VendorDAO.class);
        vendorService = new VendorService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        try {
            daoField = VendorService.class.getDeclaredField("vendorDAO");
            daoField.setAccessible(true);
            daoField.set(vendorService, vendorDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddVendor() {
        Vendor vendor = new Vendor();
        vendorService.addVendor(vendor, "ADMIN");
        verify(vendorDAOMock, times(1)).addVendor(vendor);
    }

    @Test
    void testUpdateVendor() {
        Vendor vendor = new Vendor();
        vendorService.updateVendor(vendor, "ADMIN");
        verify(vendorDAOMock, times(1)).updateVendor(vendor);
    }

    @Test
    void testDeleteVendor() {
        vendorService.deleteVendor(1, "ADMIN");
        verify(vendorDAOMock, times(1)).deleteVendor(1);
    }

    @Test
    void testGetVendorById() {
        Vendor vendor = new Vendor();
        when(vendorDAOMock.getVendorById(1)).thenReturn(vendor);
        Vendor result = vendorService.getVendorById(1);
        assertEquals(vendor, result);
    }

    @Test
    void testGetAllVendors() {
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor());
        when(vendorDAOMock.getAllVendors()).thenReturn(vendors);
        List<Vendor> result = vendorService.getAllVendors();
        assertEquals(2, result.size());
    }
}
