package de.gruppe16.stundenplaner.timetable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TimetableEventType {
    private String name;

    public TimetableEventType(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public void setName(@NotNull String name) {
        name = name.trim();
        if (name.length() > 16) {
            throw new IllegalArgumentException("");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableEventType)) return false;
        TimetableEventType that = (TimetableEventType) o;
        return this.name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
