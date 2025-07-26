package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static Connection getConnection() throws SQLException {
        String host = EnvConfig.get("DB_HOST");
        String port = EnvConfig.get("DB_PORT");
        String db = EnvConfig.get("DB_NAME");
        String user = EnvConfig.get("DB_USER");
        String pass = EnvConfig.get("DB_PASSWORD");
        String url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8", host, port, db);
        return DriverManager.getConnection(url, user, pass);
    }
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Kết nối MySQL thành công!");
        } catch (SQLException e) {
            System.err.println("Kết nối MySQL thất bại: " + e.getMessage());
        }
    }
}
