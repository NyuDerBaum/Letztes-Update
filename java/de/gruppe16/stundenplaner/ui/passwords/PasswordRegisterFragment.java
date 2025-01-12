package de.gruppe16.stundenplaner.ui.passwords;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.databinding.FragmentPasswordRegisterBinding;


public class PasswordRegisterFragment extends Fragment implements View.OnClickListener{

    private FragmentPasswordRegisterBinding binding;
    private PasswordsViewModel passwordsViewModel;
    public PasswordRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        passwordsViewModel = new ViewModelProvider(requireActivity()).get(PasswordsViewModel.class);

        binding = FragmentPasswordRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonRegisterSave.setOnClickListener(this);
        return root;
    }

    public void onClick(View v) {
        String username = binding.editTextUsername.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        String passwordRepeated = binding.editTextPasswordRepeat.getText().toString();

        if(password.isEmpty()){
            Toast.makeText(requireContext(), "Password can't be empty", Toast.LENGTH_SHORT).show();
        }else if(password.equals(passwordRepeated)){
            passwordsViewModel.getAuthManager().register(username,password);
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_passwordRegister_to_passwordsFragment);
        }else{
            Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }

    }
}