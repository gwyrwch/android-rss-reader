package com.example.rssreader.Utilities;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeToStringConverter {
    public static OffsetDateTime getDateFromString(String date) {
//        String input = "Thu, 03 Mar 2016 15:30:00 +0200";
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME ;
        return OffsetDateTime.parse(date, formatter );
    }


}
