package de.gruppe16.stundenplaner.ui.passwords;

import androidx.lifecycle.ViewModel;

import de.gruppe16.stundenplaner.password.AuthManager;
import de.gruppe16.stundenplaner.password.PasswordManager;

public class PasswordsViewModel extends ViewModel {

    private AuthManager authManager;
    private PasswordManager passwordManager;
    public void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public PasswordsViewModel() {

    }


    public PasswordManager getPasswordManager() {
        return passwordManager;
    }

    public void setPasswordManager(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }
}