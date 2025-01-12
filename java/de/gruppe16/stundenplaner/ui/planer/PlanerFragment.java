package de.gruppe16.stundenplaner.ui.planer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import android.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.widget.Button;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.timetable.TimetableWeekday;

// Der PlanerFragment ist ein Fragment, das die Benutzeroberfläche für den Stundenplan darstellt
public class PlanerFragment extends Fragment implements GridInsertionAdapter.GridItemClickListener {

    private GridInsertionAdapter adapter; // Adapter für die RecyclerView
    private final int numberOfColumns = 6;
    private static List<TimetableCell> dataList;
    private final TimetableWeekday[] weekdays = TimetableWeekday.values();

    public static List<TimetableCell> getDataList() { return dataList; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate das Layout für das Fragment
        View view = inflater.inflate(R.layout.fragment_planer, container, false);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it hasn't been granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        // Initialisiere den RecyclerView und setze das Layout
        // RecyclerView für die Anzeige der Stundenplan-Zellen
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        Resources res = getResources();
        TypedArray startTimes = res.obtainTypedArray(R.array.tableTimes);
        int timeIndex = 0;
        // Füge die Wochentage als Header zur Datenliste hinzu
        int numberOfRows = 8;
        for (int i = 0; i < numberOfColumns * numberOfRows; i++) {
            if (i == 0) {
                // Top-left corner cell
                dataList.add(new TimetableCell(""));
            } else if (i < numberOfColumns) {
                // First row: Weekdays
                int weekdayIndex = i - 1;
                dataList.add(new TimetableCell(weekdays[weekdayIndex].toString().substring(0,2)));
            } else if (i % numberOfColumns == 0) {
                // First column of each row: Start times
                dataList.add(new TimetableCell(startTimes.getString(timeIndex)));
                timeIndex++;
            } else {
                // Remaining cells: Empty
                dataList.add(new TimetableCell(""));
            }
        }

        // Setze den Adapter für den RecyclerView
        adapter = new GridInsertionAdapter(dataList, getContext(), numberOfColumns, numberOfRows, this);
        recyclerView.setAdapter(adapter);

        // Setup weekday headers with dates
        setupWeekdayHeaders(view);

        return view;
    }

    private void setupWeekdayHeaders(View view) {
        LinearLayout headerLayout = view.findViewById(R.id.weekday_header_layout);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());

        // Add an empty TextView for the first column
        TextView emptyTextView = new TextView(getContext());
        emptyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        headerLayout.addView(emptyTextView);

        // Set the calendar to the start of the week (e.g., Monday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 5; i++) { // Only iterate from Monday to Friday
            // Create a TextView for the date
            TextView dateTextView = new TextView(getContext());
            dateTextView.setText(dateFormat.format(calendar.getTime()));
            dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            dateTextView.setGravity(android.view.Gravity.CENTER); // Center the text
            dateTextView.setPadding(0, (int) (30 * getResources().getDisplayMetrics().density), 0, 0); // Add 3 cm padding to the top

            // Add the date TextView to the header layout
            headerLayout.addView(dateTextView);

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    // Methode für die Behandlung von Klicks auf Rasterelemente
    public void onGridItemClick(int position) {
        // Stelle sicher, dass der Klick nicht auf den Header bzw. die Zeit Spalte erfolgt
        if (position >= numberOfColumns && position % 6 != 0) {
            // Erstelle das Dialogfeld zur Eingabe von Modulinformationen
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View dialogView = inflater.inflate(R.layout.dialog_input_fields, null);
            // Initialisiere EditText-Felder im Dialog
            EditText editTextField1 = dialogView.findViewById(R.id.inputName);
            EditText editTextField2 = dialogView.findViewById(R.id.inputRoom);
            EditText editTextField3 = dialogView.findViewById(R.id.inputProf);
            Button buttonSelectColor = dialogView.findViewById(R.id.buttonSelectColor);
            CheckBox checkBox = dialogView.findViewById(R.id.checkBoxNotification);

            AtomicReference<Integer> selectedColor = new AtomicReference<>();
            // Standardfarbe für Module
            int DEFAULT_COLOR = Color.YELLOW;
            selectedColor.set(DEFAULT_COLOR);

            buttonSelectColor.setOnClickListener(v -> {
                ColorPickerDialogBuilder
                        .with(getContext())
                        .setTitle("Choose color")
                        .initialColor(selectedColor.get())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                // Do nothing
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int SelectedColor, Integer[] allColors) {
                                selectedColor.set(SelectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .build()
                        .show();
            });

            // Fülle die Felder mit den Eigenschaften des Objects
            editTextField1.setText(dataList.get(position).getTitle());
            editTextField2.setText(dataList.get(position).getProf());
            editTextField3.setText(dataList.get(position).getRoom());
            checkBox.setChecked(dataList.get(position).getNotification());
            if (dataList.get(position).getColor() != 0) {
                selectedColor.set(dataList.get(position).getColor());
            }

            // Erstelle das Dialogfeld mit positiven und negativen Schaltflächen
            new AlertDialog.Builder(getContext())
                    .setView(dialogView)
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Update die Werte des Objects
                        TimetableCell currentModule = dataList.get(position);
                        Resources res = getResources();
                        TypedArray startTimes = res.obtainTypedArray(R.array.tableTimes);
                        currentModule.setDay((position % 6) + 1);
                        currentModule.setStartTime(startTimes.getString((position / numberOfColumns) - 1));
                        currentModule.setTitle(editTextField1.getText().toString());
                        currentModule.setRoom(editTextField2.getText().toString());
                        currentModule.setProf(editTextField3.getText().toString());
                        currentModule.setColor(selectedColor.get());
                        currentModule.setNotification(checkBox.isChecked());
                        updateGridCell(position, currentModule);
                        currentModule.manageNotification(this.getContext());
                    })
                    .setNegativeButton("Cancel", null) // Schließe den Dialog ohne Aktion
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView(); // Aufruf der Superklasse zum Bereinigen des Views
    }

    // Aktualisiere eine Zelle im Raster basierend auf der Position
    private void updateGridCell(int position, TimetableCell module) {
        if (position >= 0 && position < dataList.size()) {
            dataList.set(position, module); // Setze das Modul in die Datenliste
            adapter.notifyItemChanged(position); // Benachrichtige den Adapter über die Änderung
        } else {
            Log.e("GalleryFragment", "Invalid position: " + position); // Fehlerprotokollierung für ungültige Position
        }
    }
}
