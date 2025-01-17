package com.omr.user_manager_v1.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {


        public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        public static boolean regexEmailValidationPattern(String emailStr) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
            return matcher.matches();
    }
}
