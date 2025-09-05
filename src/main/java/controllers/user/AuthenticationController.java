package controllers.user;

import models.main.Employee;
import services.user.AuthenticationService;

/**
 * Thin controller for authentication. Keeps View unaware of Service
 * implementation.
 */
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController() {
        this.authService = new AuthenticationService();
    }

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Attempt login; returns authenticated Employee or null.
     */
    public Employee authenticate(String username, String password) {
        return authService.authenticate(username, password);
    }
}
