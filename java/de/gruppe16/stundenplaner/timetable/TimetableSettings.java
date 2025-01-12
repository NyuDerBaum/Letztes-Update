package de.gruppe16.stundenplaner.timetable;

public class TimetableSettings {
    private int eventInterval;
    private boolean weekendEvents;

    public TimetableSettings(int eventInterval, boolean weekendEvents) {
        this.eventInterval = eventInterval;
        this.weekendEvents = weekendEvents;
    }

    public int getEventInterval() {
        return this.eventInterval;
    }
    public void setEventInterval(int eventInterval) {
        if (eventInterval < 0 || eventInterval > 60 || 60 % eventInterval != 0 || 1440 % eventInterval != 0) {
            throw new IllegalArgumentException("");
        }
        this.eventInterval = eventInterval;
    }

    public boolean isWeekendEventsEnabled() {
        return this.weekendEvents;
    }
    public void setWeekendEvents(boolean weekendEvents) {
        this.weekendEvents = weekendEvents;
    }
}
