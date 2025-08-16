import config.MySQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import views.main.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.UIManager;
import java.awt.Font;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * Thiết lập font chữ mặc định cho toàn bộ ứng dụng Swing.
     * @param font Đối tượng Font mới để áp dụng.
     */
    public static void setUIFont(java.awt.Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(font));
            }
        }
    }

    public static void main(String[] args) {
        if (isTestMode(args)) {
            // Bỏ qua khởi động UI và kiểm tra DB khi chạy unit test
            return;
        }

        // --- Bắt đầu phần thiết lập giao diện và font chữ ---
        try {
            // Thiết lập Look and Feel của hệ thống để giao diện đẹp hơn
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Thiết lập font chữ mới
            setUIFont(new Font("Arial", Font.PLAIN, 16));

        } catch (Exception e) {
            logger.error("Không thể thiết lập LookAndFeel hoặc Font: {}", e.getMessage(), e);
        }
        // --- Kết thúc phần thiết lập ---

        // Kiểm tra kết nối MySQL khi khởi động app
        try (Connection conn = MySQLConnection.getConnection()) {
            logger.info("Ket noi MySQL thành công!");
        } catch (SQLException e) {
            logger.error("Ket noi MySQL thất bại: {}", e.getMessage(), e);
            return;
        }
        
        // Chạy giao diện chính
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