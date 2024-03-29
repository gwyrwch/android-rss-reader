package com.example.rssreader.Utilities;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class OffsetDateTimeConverter {

    @TypeConverter
    public static OffsetDateTime offsetDateTimeFromTimestamp(Long value)  {
        Timestamp timestamp = new Timestamp(value);
        Instant instant = timestamp.toInstant();

        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    @TypeConverter
    public static Long offsetDateTimeToTimestamp(OffsetDateTime date) {
        if (date == null) {
            return 0L;
        }
        return Timestamp.from(date.toInstant()).getTime();
    }
}


