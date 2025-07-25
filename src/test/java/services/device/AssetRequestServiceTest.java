package services.device;

import dao.device.interfaces.AssetRequestDAO;
import models.device.AssetRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRequestServiceTest {
    private AssetRequestService assetRequestService;
    private AssetRequestDAO assetRequestDAOMock;

    @BeforeEach
    void setUp() {
        assetRequestDAOMock = Mockito.mock(AssetRequestDAO.class);
        assetRequestService = new AssetRequestService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        try {
            daoField = AssetRequestService.class.getDeclaredField("assetRequestDAO");
            daoField.setAccessible(true);
            daoField.set(assetRequestService, assetRequestDAOMock);
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
        assetRequestService.deleteAssetRequest(1, "ADMIN");
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
