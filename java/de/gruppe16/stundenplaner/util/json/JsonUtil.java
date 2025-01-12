package de.gruppe16.stundenplaner.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

import de.gruppe16.stundenplaner.util.json.adapter.LocalDateTimeAdapter;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static final Gson gsonFormatted = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public static <T> T readValue(String dataString, Class<T> type) {
        return gson.fromJson(dataString, type);
    }
    public static <T> T readValue(String dataString, Type type) {
        return gson.fromJson(dataString, type);
    }

    public static <T> T read(Reader dataReader, Class<T> type) {
        return gson.fromJson(dataReader, type);
    }
    public static <T> T read(Reader dataReader, Type type) {
        return gson.fromJson(dataReader, type);
    }

    public static String writeValue(Object object) {
        return gson.toJson(object);
    }
    public static String writeValueFormatted(Object object) {
        return gsonFormatted.toJson(object);
    }
}
