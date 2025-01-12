package de.gruppe16.stundenplaner.timetable;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TimetableEventData {
    private final Set<TimetableEventType> eventTypes;
    private final Map<TimetableWeekday, Set<TimetableEvent>> events;

    public TimetableEventData(@NotNull Set<TimetableEventType> eventTypes,
                              @NotNull Map<TimetableWeekday, Set<TimetableEvent>> events) {
        this.eventTypes = eventTypes;
        this.events = events;

        for (TimetableWeekday weekday : TimetableWeekday.values()) {
            events.computeIfAbsent(weekday, k -> new HashSet<>());
        }
    }

    public @NotNull Set<TimetableEventType> getEventTypes() {
        return Collections.unmodifiableSet(this.eventTypes);
    }
    public void addEventType(@NotNull TimetableEventType eventType) {
        this.eventTypes.add(eventType);
    }
    public void removeEventType(@NotNull TimetableEventType eventType) {
        this.eventTypes.remove(eventType);
    }

    public @NotNull Set<TimetableEvent> getEvents(@NotNull TimetableWeekday weekday) {
        return Collections.unmodifiableSet(this.events.get(weekday));
    }
    public void addEvent(@NotNull TimetableWeekday weekday, @NotNull TimetableEvent event) {
        this.events.get(weekday).add(event);
    }
    public void removeEvent(@NotNull TimetableWeekday weekday, @NotNull TimetableEvent event) {
        this.events.get(weekday).remove(event);
    }
}
