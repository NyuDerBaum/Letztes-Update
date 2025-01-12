package de.gruppe16.stundenplaner.ui.modules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import de.gruppe16.stundenplaner.ui.planer.ModulePreset;

public class ModulesViewModel extends ViewModel {

    private final MutableLiveData<List<ModulePreset>> modules = new MutableLiveData<>(new ArrayList<>());

    // Add a new module
    public void addModule(ModulePreset module) {
        List<ModulePreset> currentModules = modules.getValue();
        if (currentModules != null) {
            currentModules.add(module);
            modules.setValue(currentModules);
        }
    }

    // Edit an existing module
    public void updateModule(int index, ModulePreset updatedModule) {
        List<ModulePreset> currentModules = modules.getValue();
        if (currentModules != null && index >= 0 && index < currentModules.size()) {
            currentModules.set(index, updatedModule);
            modules.setValue(currentModules);
        }
    }

    // Delete a module
    public void deleteModule(int index) {
        List<ModulePreset> currentModules = modules.getValue();
        if (currentModules != null && index >= 0 && index < currentModules.size()) {
            currentModules.remove(index);
            modules.setValue(currentModules);
        }
    }

    // Get the list of modules as LiveData
    public LiveData<List<ModulePreset>> getModules() {
        return modules;
    }
}