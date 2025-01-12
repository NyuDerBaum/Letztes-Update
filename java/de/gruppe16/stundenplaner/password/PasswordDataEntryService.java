package de.gruppe16.stundenplaner.password;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A utility service class providing operations for PasswordDataEntry objects.
 * This class is not meant to be instantiated.
 */
public final class PasswordDataEntryService {

    // Private constructor to prevent instantiation
    private PasswordDataEntryService() {
        throw new UnsupportedOperationException("PasswordDataEntryService is a utility class and cannot be instantiated.");
    }

    /**
     * Searches for password entries whose titles start with the specified prefix, ignoring case.
     *
     * @param entries     the list of PasswordDataEntry objects to search through
     * @param titlePrefix the prefix string to match at the beginning of entry titles
     * @return a list of PasswordDataEntry objects whose titles start with the given prefix, case-insensitively.
     *         Returns an empty list if no matches are found or if the input list is empty.
     */
    public static @NotNull List<PasswordDataEntry> searchEntriesByTitle(@NotNull List<PasswordDataEntry> entries,
                                                                        @NotNull String titlePrefix) {

        if (entries.isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedPrefix = titlePrefix.toLowerCase(Locale.ROOT);

        return entries.stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getTitle().toLowerCase(Locale.ROOT).startsWith(normalizedPrefix))
                .collect(Collectors.toList());
    }
}
