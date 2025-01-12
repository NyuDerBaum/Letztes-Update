package de.gruppe16.stundenplaner.util.time;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateTimeUtil {
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static LocalDateTime getByMillis(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }
    public static long getMillisOf(@NotNull LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static String getFormattedDateTime(@NotNull LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static Optional<LocalDateTime> parseDateTime(@NotNull String string, DateTimeFormatter formatter) {
        try {
            return Optional.of(LocalDateTime.parse(string, formatter));
        } catch (DateTimeParseException exception) {
            return Optional.empty();
        }
    }
    public static String getDateString(@NotNull LocalDateTime dateTime) {
        return dateTime.format(dateFormatter);
    }
    public static String getTimeString(@NotNull LocalDateTime dateTime) {
        return dateTime.format(timeFormatter);
    }

    public static LocalDateTime clone(@NotNull LocalDateTime dateTime) {
        return LocalDateTime.of(
                dateTime.toLocalDate().withDayOfYear(dateTime.toLocalDate().getDayOfYear()),
                dateTime.toLocalTime().withNano(dateTime.toLocalTime().getNano()));
    }
}
