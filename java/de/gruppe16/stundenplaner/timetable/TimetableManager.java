package de.gruppe16.stundenplaner.timetable;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;

import de.gruppe16.stundenplaner.data.DataProvider;

public class TimetableManager {
    private static final String SETTINGS_REPO = "settings";
    private static final String EVENTS_REPO = "events";

    private final DataProvider dataProvider;

    private TimetableSettings settings;
    private TimetableEventData eventData;

    public TimetableManager(@NotNull DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void loadData() {
        this.settings = this.dataProvider.readAndWriteIfNotPresentData(SETTINGS_REPO, TimetableSettings.class,
                new TimetableSettings(15, false));
        this.eventData = this.dataProvider.readAndWriteIfNotPresentData(EVENTS_REPO, TimetableEventData.class,
                new TimetableEventData(new HashSet<>(), new EnumMap<>(TimetableWeekday.class)));
    }

    public void saveData() {
        this.dataProvider.writeData(SETTINGS_REPO, this.settings);
        this.dataProvider.writeData(EVENTS_REPO, this.eventData);
    }

    public @NotNull TimetableSettings getSettings() {
        return this.settings;
    }
    public @NotNull TimetableEventData getEventData() {
        return this.eventData;
    }
}
