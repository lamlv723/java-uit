package controllers.device;

import models.device.AssetRequest;
import models.main.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.device.AssetRequestService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRequestControllerTest {
    private AssetRequestController assetRequestController;
    private AssetRequestService assetRequestServiceMock;


    @BeforeEach
    void setUp() {
        assetRequestServiceMock = mock(AssetRequestService.class);
        assetRequestController = new AssetRequestController(assetRequestServiceMock);
    }

    @Test
    void testAddAssetRequest() {
        AssetRequest request = new AssetRequest();
        assetRequestController.addAssetRequest(request, "ADMIN");
        verify(assetRequestServiceMock, times(1)).addAssetRequest(request, "ADMIN");
    }

    @Test
    void testUpdateAssetRequest() {
        AssetRequest request = new AssetRequest();
        assetRequestController.updateAssetRequest(request, "ADMIN");
        verify(assetRequestServiceMock, times(1)).updateAssetRequest(request, "ADMIN");
    }

    @Test
    void testDeleteAssetRequest() {
        // Arrange: Chuẩn bị các tham số cần thiết
        Employee currentUser = new Employee();
        int requestId = 1;

        // Act: Gọi phương thức của đối tượng đang được test "assetRequestController"
        assetRequestController.deleteAssetRequest(requestId, currentUser);

        // Assert/Verify: Xác minh rằng controller đã gọi đến service đúng một lần
        verify(assetRequestServiceMock, times(1)).deleteAssetRequest(requestId, currentUser);
    }

    @Test
    void testGetAssetRequestById() {
        AssetRequest request = new AssetRequest();
        when(assetRequestServiceMock.getAssetRequestById(1)).thenReturn(request);
        AssetRequest result = assetRequestController.getAssetRequestById(1);
        assertEquals(request, result);
    }

    @Test
    void testGetAllAssetRequests() {
        List<AssetRequest> requests = Arrays.asList(new AssetRequest(), new AssetRequest());
        when(assetRequestServiceMock.getAllAssetRequests()).thenReturn(requests);
        List<AssetRequest> result = assetRequestController.getAllAssetRequests();
        assertEquals(2, result.size());
    }
}
