package de.gruppe16.stundenplaner.ui.passwords;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.databinding.FragmentPasswordItemBinding;
import de.gruppe16.stundenplaner.password.PasswordDataEntry;
import de.gruppe16.stundenplaner.password.PasswordManager;
import de.gruppe16.stundenplaner.password.exception.EncryptionException;
import de.gruppe16.stundenplaner.password.exception.SecurityException;


public class PasswordItemFragment extends Fragment implements View.OnClickListener{
    private PasswordsViewModel passwordsViewModel;
    private FragmentPasswordItemBinding binding;
    private PasswordManager passwordManager;
    private List<PasswordDataEntry> entries;
    private int index;
    public PasswordItemFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordsViewModel = new ViewModelProvider(requireActivity()).get(PasswordsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordItemBinding.inflate(inflater, container, false);
        binding.buttonCopyUsername.setOnClickListener(this);
        binding.buttonCopyPassword.setOnClickListener(this);
        binding.buttonDeleteEntry.setOnClickListener(this);
        index = Integer.parseInt(getArguments().getString("index"));

        passwordManager = passwordsViewModel.getPasswordManager();

        try {
            entries = passwordManager.getEntries();
            PasswordDataEntry entry = entries.get(index);

            binding.itemTitle.setText(entry.getTitle());
            binding.itemUsername.setText(entry.getUsername());
            binding.itemPassword.setText(entry.getPassword());

        } catch (SecurityException | EncryptionException exception) {
            Log.e("PasswordManager", "", exception);
        }

        return binding.getRoot();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        ClipData clip = null;
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (id == R.id.buttonCopyUsername) {
             clip = ClipData.newPlainText("Username", binding.itemUsername.getText());
        } else if(id == R.id.buttonCopyPassword){
             clip = ClipData.newPlainText("Password", binding.itemPassword.getText());
        }else if(id == R.id.buttonDeleteEntry){
            try {
                passwordManager.removeEntry(entries.get(index));
                Bundle args = new Bundle();
                args.putString("username", getArguments().getString("username"));
                args.putString("key", getArguments().getString("key"));
                NavController navController = NavHostFragment.findNavController(getParentFragment());
                navController.navigate(R.id.action_passwordItemFragment_to_passwordListFragment, args);
                return;
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (EncryptionException e) {
                throw new RuntimeException(e);
            }
        }
        clipboard.setPrimaryClip(clip);
    }

}