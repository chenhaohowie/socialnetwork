package com.sp.sn.fm.validator;

public class EmailValidator {

    private final static String EMAIL_PATTERN = "^[a-z0-9._-]+@[a-z0-9-]+(?:\\.[a-z0-9-]+)*$";

    public static boolean validate(String email) {
        if (email != null && email.matches(EMAIL_PATTERN)) {
            return true;
        }
        return false;
    }
}
