package de.gruppe16.stundenplaner.ui.passwords;

import android.os.Bundle;

import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.password.PasswordDataEntry;
import de.gruppe16.stundenplaner.password.PasswordManager;
import de.gruppe16.stundenplaner.password.exception.EncryptionException;
import de.gruppe16.stundenplaner.password.exception.SecurityException;


public class PasswordListFragment extends Fragment {
    private String username;
    private byte[] key;
    private NavController navController;
    private PasswordManager passwordManager;
    private PasswordListAdapter adapter;
    public PasswordListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            username = getArguments().getString("username");
            key = Base64.decode(getArguments().getString("key"), Base64.DEFAULT);
        }

        PasswordsViewModel passwordsViewModel = new ViewModelProvider(requireActivity()).get(PasswordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_password_list, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        navController = NavHostFragment.findNavController(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        try {
            passwordManager = new PasswordManager(requireContext(), username, key);
            passwordsViewModel.setPasswordManager(passwordManager);
            passwordManager.loadEntries();

            adapter = new PasswordListAdapter(passwordManager.getEntries(), new PasswordListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PasswordDataEntry entry, int position) {
                    Log.d("PasswordListFragment", "Item clicked: " + entry.getTitle());
                    Bundle args = new Bundle();
                    args.putString("username", username);
                    args.putString("key", getArguments().getString("key"));
                    args.putString("index", Integer.toString(position));
                    navController.navigate(R.id.action_passwordListFragment_to_passwordItemFragment, args);
                }
            });
            recyclerView.setAdapter(adapter);

        } catch (SecurityException | EncryptionException exception) {
            Log.e("PasswordManager", "Failed to load entries", exception);
        }
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);

        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("key", getArguments().getString("key"));
            navController.navigate(R.id.action_passwordListFragment_to_passwordItemEditFragment, args);
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(null);
        fab.setVisibility(View.GONE);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.fragment_password_list_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(MenuItem item) {
                Log.d("PasswordListFragment", "Delete Password clicked");


                if(item.getItemId() == android.R.id.home){
                    NavController navController = NavHostFragment.findNavController(getParentFragment());
                    Bundle args = new Bundle();
                    args.putString("username", username);
                    args.putString("key", getArguments().getString("key"));
                    navController.navigate(R.id.action_passwordListFragment_to_nav_passwords,args);
                    return true;
                }else{
                    try {
                        passwordManager.clearEntries();
                        if (adapter != null) {
                            adapter.updateData(passwordManager.getEntries());
                        }
                    } catch (SecurityException | EncryptionException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}

