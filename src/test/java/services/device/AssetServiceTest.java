package services.device;

import models.device.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import dao.device.AssetDAOImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceTest {
    private AssetService assetService;
    private AssetDAOImpl assetDAOMock;

    @BeforeEach
    void setUp() {
        assetDAOMock = Mockito.mock(AssetDAOImpl.class);
        assetService = new AssetService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        try {
            daoField = AssetService.class.getDeclaredField("assetDAO");
            daoField.setAccessible(true);
            daoField.set(assetService, assetDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddAsset() {
        Asset asset = new Asset();
        assetService.addAsset(asset);
        verify(assetDAOMock, times(1)).save(asset);
    }

    @Test
    void testGetAssetById() {
        Asset asset = new Asset();
        when(assetDAOMock.getById(1)).thenReturn(asset);
        Asset result = assetService.getAssetById(1);
        assertEquals(asset, result);
    }

    @Test
    void testGetAllAssets() {
        List<Asset> assets = Arrays.asList(new Asset(), new Asset());
        when(assetDAOMock.getAll()).thenReturn(assets);
        List<Asset> result = assetService.getAllAssets();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateAsset() {
        Asset asset = new Asset();
        assetService.updateAsset(asset);
        verify(assetDAOMock, times(1)).update(asset);
    }

    @Test
    void testDeleteAsset() {
        Asset asset = new Asset();
        assetService.deleteAsset(asset);
        verify(assetDAOMock, times(1)).delete(asset);
    }
}
