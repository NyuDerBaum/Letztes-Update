package de.gruppe16.stundenplaner.password;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import de.gruppe16.stundenplaner.password.exception.AuthenticationException;
import de.gruppe16.stundenplaner.password.exception.SecurityException;

public class AuthManager {
    private static final String USER_PREFIX = "user_";

    private final SecureStorage secureStorage;

    public AuthManager(@NotNull Context context) throws SecurityException {
        this.secureStorage = new SecureStorage(context);
    }

    public boolean register(@NotNull String username, @NotNull String password) {
        SharedPreferences prefs = this.secureStorage.getUserSharedPreferences();
        if (prefs.contains(USER_PREFIX + username + "_salt") || prefs.contains(USER_PREFIX + username + "_hash")) {
            return false;
        }

        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(password.toCharArray(), salt);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_PREFIX + username + "_salt", salt);
        editor.putString(USER_PREFIX + username + "_hash", hashedPassword);
        editor.apply();
        return true;
    }
    public byte[] authenticate(@NotNull String username, @NotNull String password) throws AuthenticationException {
        SharedPreferences prefs = secureStorage.getUserSharedPreferences();
        if (!prefs.contains(USER_PREFIX + username + "_salt") || !prefs.contains(USER_PREFIX + username + "_hash")) {
            throw new AuthenticationException("");
        }

        String salt = prefs.getString(USER_PREFIX + username + "_salt", null);
        String storedHash = prefs.getString(USER_PREFIX + username + "_hash", null);

        if (salt == null || storedHash == null) {
            throw new AuthenticationException("");
        }

        if (PasswordUtils.verifyPassword(password.toCharArray(), salt, storedHash)) {
            return this.deriveKey(password.toCharArray(), salt);
        }
        return null;
    }

    private byte[] deriveKey(char[] password, @NotNull String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, Base64.getDecoder().decode(salt), 10000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException(exception);
        }
    }
}
