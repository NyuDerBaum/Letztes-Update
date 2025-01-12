package de.gruppe16.stundenplaner.util.json.adapter;

import com.google.gson.*;

import de.gruppe16.stundenplaner.util.time.DateTimeUtil;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.format(DateTimeUtil.DEFAULT_FORMATTER));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), DateTimeUtil.DEFAULT_FORMATTER);
    }
}
