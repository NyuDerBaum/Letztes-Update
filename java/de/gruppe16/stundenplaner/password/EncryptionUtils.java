package de.gruppe16.stundenplaner.password;

import android.util.Base64;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.gruppe16.stundenplaner.password.exception.EncryptionException;

public class EncryptionUtils {
    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private static SecretKeySpec getSecretKey(byte[] key) {
        return new SecretKeySpec(key, "AES");
    }

    public static String encrypt(@NotNull String plainText, byte[] key) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key), spec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.encodeToString(combined, Base64.NO_WRAP);
        } catch (Exception exception) {
            throw new EncryptionException(exception);
        }
    }

    public static String decrypt(@NotNull String cipherText, byte[] key) throws EncryptionException {
        try {
            byte[] combined = Base64.decode(cipherText, Base64.NO_WRAP);
            byte[] iv = new byte[IV_LENGTH];
            byte[] actualCipherText = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, actualCipherText, 0, actualCipherText.length);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key), spec);
            byte[] plainTextBytes = cipher.doFinal(actualCipherText);
            return new String(plainTextBytes, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new EncryptionException(exception);
        }
    }
}
