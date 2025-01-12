package de.gruppe16.stundenplaner.data;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface DataProvider {
    <T> T readData(@NotNull String repository, @NotNull Type type);

    <T> void writeData(@NotNull String repository, @NotNull T data);

    <T> T readAndWriteIfNotPresentData(@NotNull String repository, @NotNull Type type, @NotNull T data);
}
