package services.device;

import dao.device.interfaces.AssetRequestDAO;
import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.main.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRequestServiceTest {
    private AssetRequestService assetRequestService;
    private AssetRequestDAO assetRequestDAOMock;
    private AssetRequestItemDAO assetRequestItemDAOMock;

    @BeforeEach
    void setUp() {
        assetRequestDAOMock = Mockito.mock(AssetRequestDAO.class);
        assetRequestItemDAOMock = Mockito.mock(AssetRequestItemDAO.class);
        assetRequestService = new AssetRequestService();
        // Inject mock DAOs
        try {
            java.lang.reflect.Field daoField = AssetRequestService.class.getDeclaredField("assetRequestDAO");
            daoField.setAccessible(true);
            daoField.set(assetRequestService, assetRequestDAOMock);

            java.lang.reflect.Field itemDaoField = AssetRequestService.class.getDeclaredField("assetRequestItemDAO");
            itemDaoField.setAccessible(true);
            itemDaoField.set(assetRequestService, assetRequestItemDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddAssetRequest() {
        Employee currentUser = new Employee();
        currentUser.setRole("ADMIN");

        AssetRequest request = new AssetRequest();
        assetRequestService.addAssetRequest(request, currentUser);
        verify(assetRequestDAOMock, times(1)).addAssetRequest(request);
    }

    @Test
    void testUpdateAssetRequest() {
        // Arrange
        Employee currentUser = new Employee();
        currentUser.setRole("ADMIN");

        AssetRequest requestToUpdate = new AssetRequest();
        requestToUpdate.setRequestId(1);

        // Tạo một bản ghi "hiện có" trong DB để giả lập
        AssetRequest existingRequest = new AssetRequest();
        existingRequest.setStatus("Pending");
        existingRequest.setEmployee(currentUser);

        // Dạy cho DAO trả về bản ghi "hiện có" khi được tìm kiếm
        when(assetRequestDAOMock.getAssetRequestById(1)).thenReturn(existingRequest);

        // Act
        assetRequestService.updateAssetRequest(requestToUpdate, currentUser);

        // Assert/Verify
        verify(assetRequestDAOMock, times(1)).updateAssetRequest(requestToUpdate);
    }

    @Test
    void testDeleteAssetRequest() {
        // Arrange
        Employee currentUser = new Employee();
        currentUser.setRole("ADMIN");

        AssetRequest requestToReturn = new AssetRequest();
        requestToReturn.setStatus("Pending");
        requestToReturn.setEmployee(currentUser);

        AssetRequestItem item = new AssetRequestItem();
        item.setRequestItemId(101);

        when(assetRequestDAOMock.getAssetRequestById(1)).thenReturn(requestToReturn);

        when(assetRequestItemDAOMock.getAssetRequestItemsByRequestId(1)).thenReturn(Collections.singletonList(item));

        // Act
        String result = assetRequestService.deleteAssetRequest(1, currentUser);

        // Assert
        assertNull(result, "Expected no error message on successful deletion");
        verify(assetRequestItemDAOMock, times(1)).getAssetRequestItemsByRequestId(1);
        verify(assetRequestItemDAOMock, times(1)).deleteAssetRequestItem(101);
        verify(assetRequestDAOMock, times(1)).deleteAssetRequest(1);
    }

    @Test
    void testGetAssetRequestById() {
        AssetRequest request = new AssetRequest();
        when(assetRequestDAOMock.getAssetRequestById(1)).thenReturn(request);
        AssetRequest result = assetRequestService.getAssetRequestById(1);
        assertEquals(request, result);
    }

    @Test
    void testGetAllAssetRequests() {
        // 1. Arrange: Tạo người dùng hiện tại giả
        Employee currentUser = new Employee();
        List<AssetRequest> requests = Arrays.asList(new AssetRequest(), new AssetRequest());

        // 2. Dạy cho DAO mock phải làm gì khi được gọi với currentUser
        when(assetRequestDAOMock.getAllAssetRequests(currentUser)).thenReturn(requests);

        // 3. Act: Gọi phương thức của service với currentUser
        List<AssetRequest> result = assetRequestService.getAllAssetRequests(currentUser);

        // 4. Assert & Verify
        assertEquals(2, result.size());
        verify(assetRequestDAOMock, times(1)).getAllAssetRequests(currentUser);
    }

    @Test
    void testApproveRequestChangesAsset() throws Exception {
        // Use core logic helper for unit style test
        AssetRequestService svc = assetRequestService;
        java.lang.reflect.Method m = AssetRequestService.class.getDeclaredMethod("applyBorrowApprovalCore",
                java.util.List.class, models.device.AssetRequest.class, models.main.Employee.class,
                org.hibernate.Session.class);
        m.setAccessible(true);
        models.device.Asset asset = new models.device.Asset();
        asset.setStatus("Available");
        AssetRequestItem item = new AssetRequestItem();
        item.setAsset(asset);
        AssetRequest req = new AssetRequest();
        req.setStatus("Pending");
        req.setRequestType("borrow");
        Employee approver = new Employee();
        approver.setRole("Admin");
        m.invoke(svc, java.util.Arrays.asList(item), req, approver, null);
        assertEquals("Borrowed", asset.getStatus());
        assertEquals("Approved", req.getStatus());
        assertNotNull(req.getApprovalDate());
    }

    @Test
    void testRejectRequestNoChangeAsset() throws Exception {
        AssetRequestService svc = assetRequestService;
        java.lang.reflect.Method m = AssetRequestService.class.getDeclaredMethod("applyRejectCore",
                models.device.AssetRequest.class, models.main.Employee.class);
        m.setAccessible(true);
        models.device.Asset asset = new models.device.Asset();
        asset.setStatus("Borrowed");
        AssetRequest req = new AssetRequest();
        req.setStatus("Pending");
        req.setRequestType("borrow");
        Employee approver = new Employee();
        approver.setRole("Admin");
        m.invoke(svc, req, approver);
        assertEquals("Borrowed", asset.getStatus()); // unchanged
        assertEquals("Rejected", req.getStatus());
        assertNotNull(req.getRejectedDate());
    }
}