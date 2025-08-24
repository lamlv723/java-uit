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
        AssetRequest request = new AssetRequest();
        assetRequestService.addAssetRequest(request, "ADMIN");
        verify(assetRequestDAOMock, times(1)).addAssetRequest(request);
    }

    @Test
    void testUpdateAssetRequest() {
        AssetRequest request = new AssetRequest();
        assetRequestService.updateAssetRequest(request, "ADMIN");
        verify(assetRequestDAOMock, times(1)).updateAssetRequest(request);
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
        List<AssetRequest> requests = Arrays.asList(new AssetRequest(), new AssetRequest());
        when(assetRequestDAOMock.getAllAssetRequests()).thenReturn(requests);
        List<AssetRequest> result = assetRequestService.getAllAssetRequests();
        assertEquals(2, result.size());
    }
}