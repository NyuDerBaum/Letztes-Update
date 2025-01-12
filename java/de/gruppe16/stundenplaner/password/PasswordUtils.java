package de.gruppe16.stundenplaner.password;

import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utility class for handling password-related operations such as generating salts,
 * hashing passwords, and verifying password hashes. This class uses PBKDF2 with HMAC-SHA256
 * for secure password hashing.
 */
public class PasswordUtils {
    /**
     * The length of the salt in bytes.
     */
    private static final int SALT_LENGTH = 16;
    /**
     * The number of iterations to perform in the key derivation function.
     */
    private static final int ITERATIONS = 10000;
    /**
     * The desired key length in bits for the derived hash.
     */
    private static final int KEY_LENGTH = 256;

    /**
     * Generates a random salt using a secure random number generator.
     *
     * @return A Base64-encoded string representing the generated salt.
     */
    public static @NotNull String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password using the PBKDF2WithHmacSHA256 algorithm.
     *
     * @param password The password to hash, provided as a character array.
     * @param salt     The Base64-encoded salt to use in hashing.
     * @return A Base64-encoded string representing the hashed password.
     * @throws RuntimeException If the specified algorithm is not available or the key specification is invalid.
     */
    public static String hashPassword(char[] password, @NotNull String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException("Error while hashing the password.", exception);
        }
    }

    /**
     * Verifies a password against an expected hash using the provided salt.
     *
     * @param password     The password to verify, provided as a character array.
     * @param salt         The Base64-encoded salt that was used to hash the expected password.
     * @param expectedHash The Base64-encoded expected hash to compare against.
     * @return {@code true} if the hashed password matches the expected hash; {@code false} otherwise.
     */
    public static boolean verifyPassword(char[] password, @NotNull String salt, @NotNull String expectedHash) {
        String hash = hashPassword(password, salt);
        return hash.equals(expectedHash);
    }
}
