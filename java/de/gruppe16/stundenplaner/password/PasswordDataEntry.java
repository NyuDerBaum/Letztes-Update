package de.gruppe16.stundenplaner.password;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import de.gruppe16.stundenplaner.password.exception.EncryptionException;

public class PasswordDataEntry {
    private final PasswordManager manager;

    private final String title;
    private final String username;
    private final String password;

    public PasswordDataEntry(String title, String username, String password) {
        this.manager = null;
        this.title = title;
        this.username = username;
        this.password = password;
    }

    protected PasswordDataEntry(@NotNull PasswordManager manager, String title, String username, String password) {
        this.manager = manager;
        this.title = title;
        this.username = username;
        this.password = password;
    }

    public @NotNull String getTitle() {
        return this.title;
    }
    public @NotNull String getUsername() {
        return this.username;
    }
    public @NotNull String getPassword() throws EncryptionException {
        if (this.manager == null) return this.password;
        return this.manager.decrypt(this.password);
    }

    protected boolean isPasswordEncrypted() {
        return this.manager != null;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof PasswordDataEntry)) return false;
        if (obj == this) return true;
        PasswordDataEntry data = (PasswordDataEntry) obj;
        return data.title.equals(this.title)
                && data.username.equals(this.username);
    }

    protected static PasswordDataEntry fromJSON(@NotNull PasswordManager manager, @NotNull String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);

        String title = jsonObject.getString("title");
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        return new PasswordDataEntry(manager, title, username, password);
    }
}
