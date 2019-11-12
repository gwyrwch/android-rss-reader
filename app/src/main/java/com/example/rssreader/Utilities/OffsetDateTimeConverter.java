package com.example.rssreader.Utilities;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class OffsetDateTimeConverter {

    @TypeConverter
    public OffsetDateTime offsetDateTimeFromTimestamp(Long value)  {
        Timestamp timestamp = new Timestamp(value);
        Instant instant = timestamp.toInstant();

        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    @TypeConverter
    public static Long offsetDateTimeToTimestamp(OffsetDateTime date) {
//        Instant instant = date.toInstant();
//        Timestamp ts = (Timestamp) Timestamp.from( instant );
//        return ts.getTime();

        return Timestamp.from(date.toInstant()).getTime();
    }
}
