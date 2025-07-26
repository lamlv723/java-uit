package utils;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("^\\d{9,11}$");
    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
