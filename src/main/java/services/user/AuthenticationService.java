package services.user;

import dao.main.EmployeeDAOImpl;
import dao.main.interfaces.EmployeeDAO;
import models.main.Department;
import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Xác thực: ưu tiên DB; fallback 3 tài khoản mẫu nếu chưa seed (tắt bằng -Dauth.fallback.enabled=false)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();

    public Employee authenticate(String username, String password) {
        if (isBlank(username) || isBlank(password))
            return null;

        // DB first
        Employee emp = employeeDAO.getEmployeeByUsernameAndPassword(username.trim(), password.trim());
        if (emp != null && emp.getStatus() != null && emp.getStatus().equalsIgnoreCase("Active")) {
            return emp;
        }

        if (!isFallbackEnabled()) {
            log.debug("Fallback auth disabled. Username '{}' not found or inactive.", username);
            return null;
        }

        // Fallback users
        if (match(username, password, "nguyenvana1", "matkhau1")) {
            log.warn("Using fallback hard-coded user: {}", username);
            return buildDefault(1, "Nguyen", "Van A", 1, "Admin", username, password);
        }
        if (match(username, password, "tranthib2", "matkhau2")) {
            log.warn("Using fallback hard-coded user: {}", username);
            return buildDefault(2, "Tran", "Thi B", 2, "Manager", username, password);
        }
        if (match(username, password, "levanc3", "matkhau3")) {
            log.warn("Using fallback hard-coded user: {}", username);
            return buildDefault(3, "Le", "Van C", 3, "Staff", username, password);
        }
        return null;
    }

    private boolean isFallbackEnabled() {
        // Allow disabling fallback accounts in higher environments
        return Boolean.parseBoolean(System.getProperty("auth.fallback.enabled", "true"));
    }

    private boolean match(String uIn, String pIn, String u, String p) {
        return u.equals(uIn) && p.equals(pIn);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Employee buildDefault(int id, String first, String last, Integer deptId, String role, String username,
            String password) {
        Employee e = new Employee();
        e.setEmployeeId(id); // tạm thời (DB có thể khác, chỉ dùng trong session)
        e.setFirstName(first);
        e.setLastName(last);
        e.setEmail(username + "@example.com");
        e.setRole(role);
        e.setUsername(username);
        e.setPassword(password);
        e.setStatus("Active");
        if (deptId != null) {
            Department d = new Department();
            d.setDepartmentId(deptId);
            e.setDepartment(d);
        }
        return e;
    }
}
