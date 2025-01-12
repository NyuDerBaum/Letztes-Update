package de.gruppe16.stundenplaner.data;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import de.gruppe16.stundenplaner.util.json.JsonUtil;

public class JsonFileDataProvider implements DataProvider {
    private final Context context;

    public JsonFileDataProvider(@NotNull Context context) {
        this.context = context.getApplicationContext();
    }

    private String parseJsonFileName(@NotNull String filename) {
        if (filename.endsWith(".json")) return filename;
        return filename + ".json";
    }
    private boolean fileExists(@NotNull String filename) {
        return this.context.getFileStreamPath(this.parseJsonFileName(filename)).exists();
    }

    public <T> T readData(@NotNull String filename, @NotNull Type type) {
        try (FileInputStream fileInputStream = this.context.openFileInput(this.parseJsonFileName(filename))) {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            return JsonUtil.read(new BufferedReader(inputStreamReader), type);
        } catch (JsonSyntaxException exception) {
            throw new RuntimeException("An fatal error occurred while parsing data from json", exception);
        } catch (IOException exception) {
            throw new RuntimeException("An fatal error occurred while reading data from file " + filename, exception);
        }
    }

    public <T> void writeData(@NotNull String filename, @NotNull T data) {
        try (FileOutputStream fileOutputStream = this.context.openFileOutput(this.parseJsonFileName(filename), Context.MODE_PRIVATE)) {
            fileOutputStream.write(JsonUtil.writeValue(data).getBytes());
        } catch (JsonSyntaxException exception) {
            throw new RuntimeException("An fatal error occurred while parsing data to json", exception);
        } catch (IOException exception) {
            throw new RuntimeException("An fatal error occurred while writing data to file " + filename, exception);
        }
    }

    public <T> T readAndWriteIfNotPresentData(@NotNull String filename, @NotNull Type type, @NotNull T data) {
        if (!this.fileExists(filename)) {
            this.writeData(filename, data);
        }
        return this.readData(filename, type);
    }
}
