import config.MySQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import views.user.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        FlatLightLaf.setup();
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

        // Hiển thị login trước
        javax.swing.SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }

    private static boolean isTestMode(String[] args) {
        for (String arg : args) {
            if ("--test".equals(arg))
                return true;
        }
        return false;
    }
}