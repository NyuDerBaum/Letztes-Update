package de.gruppe16.stundenplaner.password;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.gruppe16.stundenplaner.password.exception.EncryptionException;
import de.gruppe16.stundenplaner.password.exception.SecurityException;

public class PasswordManager implements AutoCloseable {
    private static final String ENTRY_PREFIX = "entry_";

    private final SharedPreferences entryPrefs;
    private final String username;
    private final byte[] key;
    private List<String> dataEntries;
    private boolean isClosed = false;

    public PasswordManager(@NotNull Context context, @NotNull String user, byte[] key) throws SecurityException {
        SecureStorage secureStorage = new SecureStorage(context);
        this.entryPrefs = secureStorage.getEntrySharedPreferences();
        this.username = user;
        this.key = Arrays.copyOf(key, key.length);
        this.dataEntries = new ArrayList<>();

    }

    /**
     * Loads password entries from shared preferences.
     *
     * @throws SecurityException if there is an issue parsing the stored data
     */
    public void loadEntries() throws SecurityException {
        List<String> entries = new ArrayList<>();

        try {
            String entriesJson = this.entryPrefs.getString(ENTRY_PREFIX + this.username, null);
            if (entriesJson != null) {
                JSONArray entriesArray = new JSONArray(entriesJson);
                for (int i = 0; i < entriesArray.length(); i++) {
                    entries.add(entriesArray.getString(i));
                }
            }
        } catch (JSONException exception) {
            throw new SecurityException("Failed to parse password entries JSON.", exception);
        }

        this.dataEntries = entries;
    }
    /**
     * Retrieves decrypted password entries.
     *
     * @return a list of decrypted PasswordDataEntry objects
     * @throws SecurityException    if there is an issue with data integrity
     * @throws EncryptionException  if decryption fails
     */
    public @NotNull List<PasswordDataEntry> getEntries() throws SecurityException, EncryptionException {
        List<PasswordDataEntry> entries = new ArrayList<>();
        try {
            for (String encryptedData : this.dataEntries) {
                String decryptedData = EncryptionUtils.decrypt(encryptedData, this.key);
                entries.add(PasswordDataEntry.fromJSON(this, decryptedData));
            }
        } catch (JSONException exception) {
            throw new SecurityException("Failed to parse decrypted password entry.", exception);
        }
        return entries;
    }

    /**
     * Adds a new password entry.
     *
     * @param entry the PasswordDataEntry to add
     * @throws SecurityException    if there is an issue with data integrity
     * @throws EncryptionException  if encryption fails
     */
    public void addEntry(@NotNull PasswordDataEntry entry) throws SecurityException, EncryptionException {
        try {
            JSONObject entryObject = new JSONObject();

            entryObject.put("title", entry.getTitle());
            entryObject.put("username", entry.getUsername());
            entryObject.put("password", EncryptionUtils.encrypt(entry.getPassword(), this.key));

            this.dataEntries.add(EncryptionUtils.encrypt(entryObject.toString(), this.key));

            this.saveData();
        } catch (JSONException exception) {
            throw new SecurityException("Failed to create JSON for new password entry.", exception);
        }
    }

    /**
     * Removes a specific password entry.
     *
     * @param data the PasswordDataEntry to remove
     * @return true if the entry was successfully removed, false otherwise
     * @throws SecurityException    if there is an issue with data integrity
     * @throws EncryptionException  if encryption fails
     */
    public boolean removeEntry(@NotNull PasswordDataEntry data) throws SecurityException, EncryptionException {
        boolean removed = false;
        try {
            Iterator<String> iterator = this.dataEntries.iterator();
            while (iterator.hasNext()) {
                String encryptedData = iterator.next();
                String decryptedData = EncryptionUtils.decrypt(encryptedData, this.key);
                PasswordDataEntry entry = PasswordDataEntry.fromJSON(this, decryptedData);

                if (entry.equals(data)) {
                    iterator.remove();
                    removed = true;
                    break;
                }
            }

            if (removed) {
                this.saveData();
            }
            return removed;
        } catch (JSONException exception) {
            throw new SecurityException("Failed to create JSON for deletion of password entry.", exception);
        }
    }

    /**
     * Removes password entries whose titles start with the given prefix.
     *
     * @param titlePrefix the prefix string to search for
     * @return true if any entries were removed, false otherwise
     * @throws SecurityException    if there is an issue with data integrity
     * @throws EncryptionException  if decryption fails
     */
    public boolean removeEntriesByTitle(@NotNull String titlePrefix) throws SecurityException, EncryptionException {
        boolean removed = false;
        try {
            Iterator<String> iterator = this.dataEntries.iterator();
            while (iterator.hasNext()) {
                String encryptedData = iterator.next();
                String decryptedData = EncryptionUtils.decrypt(encryptedData, this.key);
                JSONObject jsonObject = new JSONObject(decryptedData);

                String title = jsonObject.getString("title");
                if (title.startsWith(titlePrefix)) {
                    iterator.remove();
                    removed = true;
                }
            }

            if (removed) {
                this.saveData();
            }
            return removed;
        } catch (JSONException exception) {
            throw new SecurityException("Failed to parse decrypted password entry during removal by prefix.", exception);
        }
    }

    /**
     * Clears all password entries.
     *
     * @return true if entries were cleared successfully
     * @throws SecurityException    if any decryption failures occur
     */
    public boolean clearEntries() throws SecurityException {
        int updates = 0, failedDecryptions = 0;

        Iterator<String> iterator = this.dataEntries.iterator();
        while (iterator.hasNext()) {
            String encryptedData = iterator.next();
            try {
                EncryptionUtils.decrypt(encryptedData, this.key);
                iterator.remove();
                updates++;
            } catch (EncryptionException ignored) {
                failedDecryptions++;
            }
        }

        if (updates != 0) {
            this.saveData();
        }
        if (failedDecryptions != 0) {
            throw new SecurityException("Failed to decrypt some password entries during clearing.", new EncryptionException("Decryption failed for some entries."));
        }
        return true;
    }

    /**
     * Saves the current state of dataEntries to shared preferences.
     *
     * @throws SecurityException if saving fails
     */
    private void saveData() throws SecurityException {
        try {
            JSONArray entriesArray = new JSONArray();

            for (String encryptedData : this.dataEntries) {
                entriesArray.put(encryptedData);
            }

            SharedPreferences.Editor editor = this.entryPrefs.edit();
            editor.putString(ENTRY_PREFIX + this.username, entriesArray.toString());
            editor.apply();
        } catch (Exception exception) {
            throw new SecurityException("Failed to save password entries to shared preferences.", exception);
        }
    }

    /**
     * Decrypts a given value using the encryption key.
     *
     * @param value the encrypted string
     * @return the decrypted string
     * @throws EncryptionException if decryption fails
     */
    protected @NotNull String decrypt(@NotNull String value) throws EncryptionException {
        return EncryptionUtils.decrypt(value, this.key);
    }

    /**
     * Closes the PasswordManager and clears the encryption key from memory.
     */
    @Override
    public void close() {
        if (!isClosed) {
            Arrays.fill(this.key, (byte) 0);
            isClosed = true;
        }
    }
}
