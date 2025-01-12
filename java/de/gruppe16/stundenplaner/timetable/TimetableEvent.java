package de.gruppe16.stundenplaner.timetable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

import de.gruppe16.stundenplaner.util.StringUtil;

public class TimetableEvent {
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

    private String startTime;
    private String endTime;
    private String title;
    private String type;
    private String room;
    private String lecturer;

    public TimetableEvent(@NotNull String startTime, @NotNull String endTime, @NotNull String title, @NotNull String type, @NotNull String room, @NotNull String lecturer) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.type = type;
        this.room = room;
        this.lecturer = lecturer;
    }

    public @NotNull String getStartTime() {
        return this.startTime;
    }
    public @NotNull String getEndTime() {
        return this.endTime;
    }
    public @NotNull String getTitle() {
        return this.title;
    }
    public @NotNull String getType() {
        return this.type;
    }
    public @NotNull String getRoom() {
        return this.room;
    }
    public @NotNull String getLecturer() {
        return this.lecturer;
    }

    public void setTitle(String title) {
        title = title.trim();
        if (title.length() > 16) {
            throw new IllegalArgumentException("");
        }
        this.title = title;
    }
    public void setType(String type) {
        type = type.trim();
        if (type.length() > 16) {
            throw new IllegalArgumentException("");
        }
        this.type = type;
    }
    public void setRoom(String room) {
        room = room.trim();
        if (room.length() > 16) {
            throw new IllegalArgumentException("");
        }
        this.room = room;
    }
    public void setLecturer(String lecturer) {
        lecturer = lecturer.trim();
        if (lecturer.length() > 16) {
            throw new IllegalArgumentException("");
        }
        this.lecturer = lecturer;
    }
    public void setStartTime(String startTime) {
        if (!StringUtil.isMatchingPattern(startTime, TIME_PATTERN)) {
            throw new IllegalArgumentException("");
        }
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        if (!StringUtil.isMatchingPattern(endTime, TIME_PATTERN)) {
            throw new IllegalArgumentException("");
        }
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableEvent)) return false;
        TimetableEvent that = (TimetableEvent) o;
        return Objects.equals(this.startTime, that.startTime)
                && Objects.equals(this.endTime, that.endTime)
                && this.title.equalsIgnoreCase(that.title)
                && this.type.equals(that.type)
                && this.room.equalsIgnoreCase(that.room)
                && this.lecturer.equalsIgnoreCase(that.lecturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, title, type, room, lecturer);
    }
}
