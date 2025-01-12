package de.gruppe16.stundenplaner.ui.bundesland;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.databinding.FragmentBundeslandBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BundeslandFragment extends Fragment {

    private FragmentBundeslandBinding binding;
    private Map<String, String[][]> feiertageMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBundeslandBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the map with holidays
        initializeFeiertageMap();

        // Set up the spinner
        Spinner spinner = binding.spinnerBundesland;
        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.bundeslaender_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedBundesland = parent.getItemAtPosition(position).toString();
                    displayFeiertage(selectedBundesland);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
        }
    }

    private void initializeFeiertageMap() {
        feiertageMap = new HashMap<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        feiertageMap.put("Baden-Württemberg", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {"06.01." + year, "  Heilige Drei Könige"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"01.11." + year, "  Allerheiligen"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Bayern", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {"06.01." + year, "  Heilige Drei Könige"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"15.08." + year, "  Mariä Himmelfahrt"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"01.11." + year, "  Allerheiligen"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Berlin", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {"08.03." + year, "  Internationaler Frauentag"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Brandenburg", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Bremen", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Hamburg", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Hessen", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Mecklenburg-Vorpommern", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Niedersachsen", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Nordrhein-Westfalen", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"01.11." + year, "  Allerheiligen"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Rheinland-Pfalz", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"01.11." + year, "  Allerheiligen"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Saarland", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {"06.01." + year, "  Heilige Drei Könige"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {calculateEasterSunday(year, 60), "  Fronleichnam"},
                {"15.08." + year, "  Mariä Himmelfahrt"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"01.11." + year, "  Allerheiligen"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Sachsen", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Sachsen-Anhalt", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {"06.01." + year, "  Heilige Drei Könige"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Schleswig-Holstein", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });

        feiertageMap.put("Thüringen", new String[][]{
                {"01.01." + year, "  Neujahr"},
                {calculateEasterSunday(year, -2), "  Karfreitag"},
                {calculateEasterSunday(year, 1), "  Ostermontag"},
                {"01.05." + year, "  Tag der Arbeit"},
                {calculateEasterSunday(year, 39), "  Christi Himmelfahrt"},
                {calculateEasterSunday(year, 50), "  Pfingstmontag"},
                {"20.09." + year, "  Weltkindertag"},
                {"03.10." + year, "  Tag der Deutschen Einheit"},
                {"31.10." + year, "  Reformationstag"},
                {"25.12." + year, "  1. Weihnachtstag"},
                {"26.12." + year, "  2. Weihnachtstag"}
        });
    }

    private String calculateEasterSunday(int year, int offset) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;

        Calendar easterSunday = Calendar.getInstance();
        easterSunday.set(year, month - 1, day);
        easterSunday.add(Calendar.DAY_OF_MONTH, offset);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(easterSunday.getTime());
    }

    private void displayFeiertage(String bundesland) {
        TableLayout tableLayout = binding.tableFeiertage;
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1); // Clear previous rows

        String[][] feiertage = feiertageMap.get(bundesland);
        if (feiertage != null) {
            for (String[] feiertag : feiertage) {
                TableRow tableRow = new TableRow(getContext());
                TextView datumTextView = new TextView(getContext());
                datumTextView.setText(feiertag[0]);
                datumTextView.setPadding(8, 8, 8, 8);
                datumTextView.setTextSize(18);
                TextView feiertagTextView = new TextView(getContext());
                feiertagTextView.setText(feiertag[1]);
                feiertagTextView.setPadding(8, 8, 8, 8);
                feiertagTextView.setTextSize(18);
                tableRow.addView(datumTextView);
                tableRow.addView(feiertagTextView);
                tableLayout.addView(tableRow);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
