package de.gruppe16.stundenplaner.ui.passwords;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.databinding.FragmentPasswordItemEditBinding;
import de.gruppe16.stundenplaner.password.PasswordDataEntry;
import de.gruppe16.stundenplaner.password.PasswordManager;
import de.gruppe16.stundenplaner.password.exception.EncryptionException;
import de.gruppe16.stundenplaner.password.exception.SecurityException;


public class PasswordItemEditFragment extends Fragment implements View.OnClickListener{

    private PasswordsViewModel passwordsViewModel;
    private FragmentPasswordItemEditBinding binding;
    private String username;
    private byte[] key;

    public PasswordItemEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordsViewModel = new ViewModelProvider(requireActivity()).get(PasswordsViewModel.class);
        if (getArguments() != null) {
            username = getArguments().getString("username");
            key = Base64.decode(getArguments().getString("key"), Base64.DEFAULT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordItemEditBinding.inflate(inflater, container, false);
        binding.buttonSavePassword.setOnClickListener(this);

        // Add TextWatcher to update the ProgressBar dynamically
        binding.itemPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // When the text changes, update the ProgressBar
                String password = charSequence.toString();
                updatePasswordStrength(password); // Call the method to update the progress bar
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No implementation needed
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        PasswordManager passwordManager = passwordsViewModel.getPasswordManager();
        try {
            String inputTitle = binding.itemTitle.getText().toString();
            String inputUsername = binding.itemUsername.getText().toString();
            String inputPassword = binding.itemPassword.getText().toString();
            String inputPasswordRepeat = binding.itemPasswordRepeat.getText().toString();
            if(inputTitle.isEmpty() || inputPassword.isEmpty() || inputUsername.isEmpty()){
                throw new NullPointerException();
            }
            if(!inputPassword.equals(inputPasswordRepeat)){
                throw new ArithmeticException();
            }
            passwordManager.addEntry(new PasswordDataEntry(inputTitle, inputUsername, inputPassword));
            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("key", getArguments().getString("key"));
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_passwordsItemEditFragment_to_passwordListFragment, args);

        } catch (SecurityException | EncryptionException exception) {
            Log.e("PasswordManager", "", exception);
        } catch (NullPointerException e){
            Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } catch (ArithmeticException e){
            Toast.makeText(requireContext(), "Passwords are not the same", Toast.LENGTH_SHORT).show();
        }
    }
    private void updatePasswordStrength(String password) {
        int strength = 25;
        int color = R.color.colorWeak;

        if (password.length() >= 8) {
            strength = 50;
            color = R.color.colorMedium;
        }
        if (password.length() >= 16) {
            strength = 75;
            color = R.color.colorStrong;
        }
        if (password.length() >= 24) {
            strength = 100;
            color = R.color.colorVeryStrong;
        }
        // Update progress bar
        binding.passwordStrengthBar.setProgress(strength);
        int resolvedColor = getResources().getColor(color, getActivity().getTheme());

        // Update progress bar
        binding.passwordStrengthBar.setProgress(strength);

        // Apply color filter to the progress drawable
        ColorFilter colorFilter = new PorterDuffColorFilter(resolvedColor, PorterDuff.Mode.SRC_IN);
        Drawable progressDrawable = binding.passwordStrengthBar.getProgressDrawable();
        progressDrawable.setColorFilter(colorFilter);
    }

}