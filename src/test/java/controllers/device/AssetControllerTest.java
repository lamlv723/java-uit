package controllers.device;

import models.device.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.device.AssetService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetControllerTest {
    private AssetController assetController;
    private AssetService assetServiceMock;

    @BeforeEach
    void setUp() {
        assetServiceMock = mock(AssetService.class);
        assetController = new AssetController(assetServiceMock);
    }

    @Test
    void testCreateAsset() {
        Asset asset = new Asset();
        assetController.addAsset(asset);
        verify(assetServiceMock, times(1)).addAsset(asset);
    }

    @Test
    void testUpdateAsset() {
        Asset asset = new Asset();
        assetController.updateAsset(asset);
        verify(assetServiceMock, times(1)).updateAsset(asset);
    }

    @Test
    void testDeleteAsset() {
        Asset asset = new Asset();
        assetController.deleteAsset(asset);
        verify(assetServiceMock, times(1)).deleteAsset(asset);
    }

    @Test
    void testGetAsset() {
        Asset asset = new Asset();
        when(assetServiceMock.getAssetById(1)).thenReturn(asset);
        Asset result = assetController.getAssetById(1);
        assertEquals(asset, result);
    }

    @Test
    void testGetAllAssets() {
        List<Asset> assets = Arrays.asList(new Asset(), new Asset());
        when(assetServiceMock.getAllAssets()).thenReturn(assets);
        List<Asset> result = assetController.getAllAssets();
        assertEquals(2, result.size());
    }
}
