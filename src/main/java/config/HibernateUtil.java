package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import io.github.cdimascio.dotenv.Dotenv;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            Configuration config = new Configuration();
            config.configure(); // load hibernate.cfg.xml

            // Ghi đè các property từ .env
            config.setProperty("hibernate.connection.username", dotenv.get("DB_USER"));
            config.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));
            config.setProperty("hibernate.connection.url",
                    "jdbc:mysql://" + dotenv.get("DB_HOST") + ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME")
                            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");

            sessionFactory = config.buildSessionFactory();
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
