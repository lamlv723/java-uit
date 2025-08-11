package controllers.user;

import models.main.Employee;

public class UserSession {
    private static UserSession instance;
    private Employee loggedInEmployee;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInEmployee(Employee employee) {
        this.loggedInEmployee = employee;
    }

    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public String getCurrentUserRole() {
        if (loggedInEmployee != null) {
            return loggedInEmployee.getRole();
        }
        return null;
    }

    public void clearSession() {
        loggedInEmployee = null;
    }
}