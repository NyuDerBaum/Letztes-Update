package de.gruppe16.stundenplaner.ui.passwords;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.data.DataProvider;
import de.gruppe16.stundenplaner.data.JsonFileDataProvider;
import de.gruppe16.stundenplaner.databinding.FragmentPasswordsBinding;
import de.gruppe16.stundenplaner.password.AuthManager;
import de.gruppe16.stundenplaner.password.exception.AuthenticationException;
import de.gruppe16.stundenplaner.password.exception.SecurityException;
import de.gruppe16.stundenplaner.timetable.TimetableManager;
import de.gruppe16.stundenplaner.util.Logger;

public class PasswordsFragment extends Fragment implements View.OnClickListener {
    private PasswordsViewModel passwordsViewModel;
    private FragmentPasswordsBinding binding;

    public PasswordsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        passwordsViewModel = new ViewModelProvider(requireActivity()).get(PasswordsViewModel.class);

        try {
            passwordsViewModel.setAuthManager(new AuthManager(requireContext()));
        } catch (SecurityException exception) {
            Log.e("Authentication", "", exception);
            return null;
        }
        binding = FragmentPasswordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        DataProvider dataProvider = new JsonFileDataProvider(requireContext());
        TimetableManager timetableManager = new TimetableManager(dataProvider);

        Logger.info("Loading Data...");

        try {
            timetableManager.loadData();
        } catch (Exception exception) {
            Logger.error(exception.getMessage(), exception);
        }

        binding.buttonLogin.setOnClickListener(this);
        binding.buttonRegister.setOnClickListener(this);

        return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        binding.editTextUsername.setText(null);
        binding.editTextPassword.setText(null);
    }
    @Override
    public void onClick(View v) {
        String username = binding.editTextUsername.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        int id = v.getId();
        if (id == R.id.buttonLogin) {
                try {
                    byte[] key = passwordsViewModel.getAuthManager().authenticate(username, password);
                    if(key == null){
                        Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("Authentication", "Success");

                    Bundle args = new Bundle();
                    args.putString("username", username);
                    args.putString("password", password);
                    args.putString("key", Base64.encodeToString(key, Base64.DEFAULT));

                    NavController navController = NavHostFragment.findNavController(this);
                    navController.navigate(R.id.action_passwordsFragment_to_passwordListFragment, args);
                } catch (AuthenticationException e) {
                    Toast.makeText(requireContext(), "Credentials wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Authentication", "", e);
                }

        } else if (id == R.id.buttonRegister) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_passwords_to_passwordRegisterFragment);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}