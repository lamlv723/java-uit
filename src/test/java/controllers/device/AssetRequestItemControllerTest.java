package controllers.device;

import models.device.AssetRequestItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.device.AssetRequestItemService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRequestItemControllerTest {
    private AssetRequestItemController assetRequestItemController;
    private AssetRequestItemService assetRequestItemServiceMock;

    @BeforeEach
    void setUp() {
        assetRequestItemServiceMock = mock(AssetRequestItemService.class);
        assetRequestItemController = new AssetRequestItemController(assetRequestItemServiceMock);
    }

    @Test
    void testAddAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        assetRequestItemController.addAssetRequestItem(item, "ADMIN");
        verify(assetRequestItemServiceMock, times(1)).addAssetRequestItem(item, "ADMIN");
    }

    @Test
    void testUpdateAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        assetRequestItemController.updateAssetRequestItem(item, "ADMIN");
        verify(assetRequestItemServiceMock, times(1)).updateAssetRequestItem(item, "ADMIN");
    }

    @Test
    void testDeleteAssetRequestItem() {
        assetRequestItemController.deleteAssetRequestItem(1, "ADMIN");
        verify(assetRequestItemServiceMock, times(1)).deleteAssetRequestItem(1, "ADMIN");
    }

    @Test
    void testGetAssetRequestItemById() {
        AssetRequestItem item = new AssetRequestItem();
        when(assetRequestItemServiceMock.getAssetRequestItemById(1)).thenReturn(item);
        AssetRequestItem result = assetRequestItemController.getAssetRequestItemById(1);
        assertEquals(item, result);
    }

    @Test
    void testGetAllAssetRequestItems() {
        List<AssetRequestItem> items = Arrays.asList(new AssetRequestItem(), new AssetRequestItem());
        when(assetRequestItemServiceMock.getAllAssetRequestItems()).thenReturn(items);
        List<AssetRequestItem> result = assetRequestItemController.getAllAssetRequestItems();
        assertEquals(2, result.size());
    }
}
