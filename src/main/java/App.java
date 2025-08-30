import config.MySQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import views.user.LoginView;
import views.main.MainView;
import services.user.AuthenticationService;
import controllers.user.UserSession;
import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    // Dev auto-login: enable with -Ddev.autoLogin=true (and optional
    // -Ddev.autoLogin.user / -Ddev.autoLogin.pass). Never enable in production.
    private static boolean isDevAutoLoginEnabled() {
        return Boolean.getBoolean("dev.autoLogin");
    }

    private static String devAutoUser() {
        return System.getProperty("dev.autoLogin.user", "nguyenvana1");
    }

    private static String devAutoPass() {
        return System.getProperty("dev.autoLogin.pass", "matkhau1");
    }

    public static void main(String[] args) {
        if (isTestMode(args))
            return; // skip UI during tests
        // Check MySQL connection early
        try (Connection conn = MySQLConnection.getConnection()) {
            logger.info("Ket noi MySQL thành công!");
        } catch (SQLException e) {
            logger.error("Ket noi MySQL thất bại: {}", e.getMessage(), e);
            return;
        }

        // Init Look & Feel
        FlatLightLaf.setup();

        if (isDevAutoLoginEnabled()) {
            try {
                AuthenticationService auth = new AuthenticationService();
                Employee emp = auth.authenticate(devAutoUser(), devAutoPass());
                if (emp != null) {
                    UserSession.getInstance().setLoggedInEmployee(emp);
                    logger.warn("[DEV_AUTO_LOGIN] Enabled via -Ddev.autoLogin. DO NOT USE IN PRODUCTION.");
                    javax.swing.SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
                    return; // skip login view
                }
                logger.warn("[DEV_AUTO_LOGIN] Credentials invalid; showing normal LoginView.");
            } catch (Exception ex) {
                logger.error("[DEV_AUTO_LOGIN] Error performing auto-login: {}", ex.getMessage(), ex);
            }
        }

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