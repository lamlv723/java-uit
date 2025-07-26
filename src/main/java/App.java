import config.MySQLConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        // Kiểm tra kết nối MySQL khi khởi động app
        try (Connection conn = MySQLConnection.getConnection()) {
            System.out.println("Ket noi MySQL thành công!");
        } catch (SQLException e) {
            System.err.println("Ket noi MySQL thất bại: " + e.getMessage());
            return;
        }
        // ...existing code...
        System.out.println("Hello world!");
    }
}
