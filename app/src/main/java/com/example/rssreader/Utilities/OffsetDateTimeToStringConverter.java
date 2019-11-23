package com.example.rssreader.Utilities;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class OffsetDateTimeToStringConverter {
    public static OffsetDateTime getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME ;
        return OffsetDateTime.parse(date, formatter );
    }

    private static final DateTimeFormatter FORMATTER;

    static {
        FORMATTER = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM uuuu HH:mm")
                .toFormatter();
    }

    public static String getStringFromDate(OffsetDateTime date) {
        if (date == null) {
            return null;
        }

        return FORMATTER.format(date);
    }


}
