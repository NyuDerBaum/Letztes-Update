package de.gruppe16.stundenplaner.util;

import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isMatchingPattern(String string, Pattern pattern) {
        return pattern.matcher(string).matches();
    }
    public static boolean isMatchingPattern(String string, String regex) {
        return isMatchingPattern(string, Pattern.compile(regex));
    }
}
