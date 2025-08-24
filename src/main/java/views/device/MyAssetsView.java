package views.device;

import controllers.device.AssetController;
import controllers.user.UserSession;
import models.device.Asset;
import models.main.Employee;
import services.device.AssetService;
import views.device.components.AssetTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyAssetsView extends JFrame {

    private AssetTable assetTable;
    private AssetService assetService;

    public MyAssetsView() {
        setTitle("Tài sản của tôi");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetService = new AssetService();
        assetTable = new AssetTable();
        assetTable.setEnabled(false); // Make table read-only

        JScrollPane scrollPane = new JScrollPane(assetTable);
        add(scrollPane, BorderLayout.CENTER);

        loadAssets();
    }

    private void loadAssets() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        if (currentUser != null) {
            List<Asset> myAssets = assetService.getBorrowedAssetsByEmployeeId(currentUser.getEmployeeId());
            assetTable.setAssetData(myAssets);
        }
    }
}