package config;

import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class EnvConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvConfig.class);
    private static final String ENV_PATH = ".env";
    private static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(ENV_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            logger.error("Không thể đọc file .env: {}", e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
