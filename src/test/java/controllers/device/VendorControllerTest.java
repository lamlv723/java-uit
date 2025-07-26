package controllers.device;

import models.device.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.device.VendorService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendorControllerTest {
    private VendorController vendorController;
    private VendorService vendorServiceMock;

    @BeforeEach
    void setUp() {
        vendorServiceMock = mock(VendorService.class);
        vendorController = new VendorController(vendorServiceMock);
    }

    @Test
    void testAddVendor() {
        Vendor vendor = new Vendor();
        vendorController.addVendor(vendor, "ADMIN");
        verify(vendorServiceMock, times(1)).addVendor(vendor, "ADMIN");
    }

    @Test
    void testUpdateVendor() {
        Vendor vendor = new Vendor();
        vendorController.updateVendor(vendor, "ADMIN");
        verify(vendorServiceMock, times(1)).updateVendor(vendor, "ADMIN");
    }

    @Test
    void testDeleteVendor() {
        vendorController.deleteVendor(1, "ADMIN");
        verify(vendorServiceMock, times(1)).deleteVendor(1, "ADMIN");
    }

    @Test
    void testGetVendorById() {
        Vendor vendor = new Vendor();
        when(vendorServiceMock.getVendorById(1)).thenReturn(vendor);
        Vendor result = vendorController.getVendorById(1);
        assertEquals(vendor, result);
    }

    @Test
    void testGetAllVendors() {
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor());
        when(vendorServiceMock.getAllVendors()).thenReturn(vendors);
        List<Vendor> result = vendorController.getAllVendors();
        assertEquals(2, result.size());
    }
}