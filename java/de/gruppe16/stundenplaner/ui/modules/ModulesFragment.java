package de.gruppe16.stundenplaner.ui.modules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.databinding.FragmentModulesBinding;

import de.gruppe16.stundenplaner.ui.planer.ModulePreset;

public class ModulesFragment extends Fragment {
    private ModulesViewModel moduleViewModel;

    private EditText titleEditText;
    private EditText profEditText;
    private EditText colorEditText;
    private Button addModuleButton;
    private ListView modulesListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modules, container, false);

        // Initialize UI elements
        titleEditText = root.findViewById(R.id.titleEditText);
        profEditText = root.findViewById(R.id.profEditText);
        colorEditText = root.findViewById(R.id.colorEditText);
        addModuleButton = root.findViewById(R.id.addModuleButton);
        modulesListView = root.findViewById(R.id.modulesListView);

        // Set up ViewModel
        moduleViewModel = new ViewModelProvider(this).get(ModulesViewModel.class);

        // Observe changes to the list of modules and update the UI
        moduleViewModel.getModules().observe(getViewLifecycleOwner(), this::updateModulesList);

        // Handle Add Module button click
        addModuleButton.setOnClickListener(view -> addModule());

        return root;
    }

    // Adds a new module to the list
    private void addModule() {
        String title = titleEditText.getText().toString();
        String prof = profEditText.getText().toString();
        int color;

        try {
            color = Integer.parseInt(colorEditText.getText().toString());
        } catch (NumberFormatException e) {
            colorEditText.setError("Enter a valid color (e.g., an integer)");
            return;
        }

        if (title.isEmpty() || prof.isEmpty()) {
            // Handle empty fields
            if (title.isEmpty()) titleEditText.setError("Title is required");
            if (prof.isEmpty()) profEditText.setError("Professor is required");
            return;
        }

        // Add the new module to the ViewModel
        moduleViewModel.addModule(new ModulePreset(title, prof, color));

        // Clear the input fields
        titleEditText.setText("");
        profEditText.setText("");
        colorEditText.setText("");
    }

    // Updates the ListView when the list of modules changes
    private void updateModulesList(List<ModulePreset> modules) {
        List<String> moduleTitles = new ArrayList<>();
        for (ModulePreset module : modules) {
            moduleTitles.add(module.getTitle() + " - " + module.getProf());
        }

        // Simple ListView adapter (can be replaced with a custom adapter for better UI)
        modulesListView.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                moduleTitles
        ));
    }
}