package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLConnection {
    private static final Logger logger = LoggerFactory.getLogger(MySQLConnection.class);

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
            logger.info("Kết nối MySQL thành công!");
        } catch (SQLException e) {
            logger.error("Kết nối MySQL thất bại: {}", e.getMessage(), e);
        }
    }
}
