package com.lttbdd.hrmsystem.utils;

public class ValidationUtils {

    public static boolean isValidEmail(String v) {
        return v != null && android.util.Patterns.EMAIL_ADDRESS.matcher(v).matches();
    }

    public static boolean isValidPhone(String v) {
        return v != null && v.matches("^(0[3|5|7|8|9])([0-9]{8})$");
    }

    public static boolean isValidPassword(String v) {
        return v != null && v.length() >= 6;
    }

    public static boolean isNotEmpty(String v) {
        return v != null && !v.trim().isEmpty();
    }

    public static boolean isValidCard(String v) {
        return v != null && v.matches("^[0-9]{9}$|^[0-9]{12}$");
    }

    public static String validateRegister(String email, String password, String confirmPwd, String fullName) {
        if (!isNotEmpty(fullName)) {
            return "Họ tên không được để trống";
        }

        if (!isValidEmail(email)) {
            return "Email không hợp lệ";
        }

        if (!isValidPassword(password)) {
            return "Mậu khẩu tối thiểu 6 ký tự";
        }

        if (!password.equals(confirmPwd)) {
            return "Mật khẩu xác nhận không khớp";
        }

        return null;
    }
}
