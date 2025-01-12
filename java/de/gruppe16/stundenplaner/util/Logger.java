package de.gruppe16.stundenplaner.util;

import android.util.Log;

public class Logger {
    private static final String TAG = "Stundenplaner";

    public static void info(String string) {
        Log.i(TAG, string);
    }
    public static void warn(String string) {
        Log.w(TAG, string);
    }
    public static void warn(Throwable exception) {
        Log.w(TAG, exception);
    }
    public static void warn(String string, Throwable exception) {
        Log.w(TAG, string, exception);
    }
    public static void error(String string) {
        Log.e(TAG, string);
    }
    public static void error(String string, Throwable exception) {
        Log.e(TAG, string, exception);
    }
}
