package io.tidepool.api.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GsonDateAdapter implements JsonSerializer<Date> {

    private final DateFormat dateFormat;

    public GsonDateAdapter(boolean makeUTC, boolean showTimezone) {
        String dateFormatString = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
        if (!showTimezone) {
            dateFormatString = "yyyy-MM-dd'T'HH:mm:ss";
        } else if (makeUTC) {
            dateFormatString = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        dateFormat = new SimpleDateFormat(dateFormatString, Locale.US);
        if (makeUTC) {
            // This is the key line which converts the date to UTC which cannot be accessed with the default serializer
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
    }

    @Override public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(dateFormat.format(date));
    }
}
