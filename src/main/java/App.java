import config.MySQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import views.main.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        if (isTestMode(args)) {
            // Bỏ qua khởi động UI và kiểm tra DB khi chạy unit test
            return;
        }
        // Kiểm tra kết nối MySQL khi khởi động app
        try (Connection conn = MySQLConnection.getConnection()) {
            logger.info("Ket noi MySQL thành công!");
        } catch (SQLException e) {
            logger.error("Ket noi MySQL thất bại: {}", e.getMessage(), e);
            return;
        }
        javax.swing.SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }

    private static boolean isTestMode(String[] args) {
        for (String arg : args) {
            if ("--test".equals(arg))
                return true;
        }
        return false;
    }
}