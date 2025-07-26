package services.device;

import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequestItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRequestItemServiceTest {
    private AssetRequestItemService assetRequestItemService;
    private AssetRequestItemDAO assetRequestItemDAOMock;

    @BeforeEach
    void setUp() {
        assetRequestItemDAOMock = Mockito.mock(AssetRequestItemDAO.class);
        assetRequestItemService = new AssetRequestItemService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        try {
            daoField = AssetRequestItemService.class.getDeclaredField("assetRequestItemDAO");
            daoField.setAccessible(true);
            daoField.set(assetRequestItemService, assetRequestItemDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        assetRequestItemService.addAssetRequestItem(item, "ADMIN");
        verify(assetRequestItemDAOMock, times(1)).addAssetRequestItem(item);
    }

    @Test
    void testUpdateAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        assetRequestItemService.updateAssetRequestItem(item, "ADMIN");
        verify(assetRequestItemDAOMock, times(1)).updateAssetRequestItem(item);
    }

    @Test
    void testDeleteAssetRequestItem() {
        assetRequestItemService.deleteAssetRequestItem(1, "ADMIN");
        verify(assetRequestItemDAOMock, times(1)).deleteAssetRequestItem(1);
    }

    @Test
    void testGetAssetRequestItemById() {
        AssetRequestItem item = new AssetRequestItem();
        when(assetRequestItemDAOMock.getAssetRequestItemById(1)).thenReturn(item);
        AssetRequestItem result = assetRequestItemService.getAssetRequestItemById(1);
        assertEquals(item, result);
    }

    @Test
    void testGetAllAssetRequestItems() {
        List<AssetRequestItem> items = Arrays.asList(new AssetRequestItem(), new AssetRequestItem());
        when(assetRequestItemDAOMock.getAllAssetRequestItems()).thenReturn(items);
        List<AssetRequestItem> result = assetRequestItemService.getAllAssetRequestItems();
        assertEquals(2, result.size());
    }
}
