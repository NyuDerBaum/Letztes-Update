package de.gruppe16.stundenplaner.password;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import de.gruppe16.stundenplaner.password.exception.SecurityException;

public class SecureStorage {
    private static final String USER_PREFS_FILENAME = "user_secure_prefs";
    private static final String ENTRY_PREFS_FILENAME = "entry_secure_prefs";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences entrySharedPreferences;

    public SecureStorage(@NotNull Context context) throws SecurityException {
        try {
            MasterKey masterkey = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            this.sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    USER_PREFS_FILENAME,
                    masterkey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            this.entrySharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    ENTRY_PREFS_FILENAME,
                    masterkey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException exception) {
            throw new SecurityException(exception);
        }
    }

    public @NotNull SharedPreferences getUserSharedPreferences() {
        return this.sharedPreferences;
    }

    public @NotNull SharedPreferences getEntrySharedPreferences() {
        return this.entrySharedPreferences;
    }
}
