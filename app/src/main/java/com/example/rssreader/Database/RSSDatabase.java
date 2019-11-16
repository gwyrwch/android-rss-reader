package com.example.rssreader.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.OffsetDateTimeConverter;
import com.example.rssreader.Utilities.OffsetDateTimeToStringConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Database(entities = {RSSItem.class}, version = 1)
@TypeConverters({OffsetDateTimeConverter.class})
public abstract class RSSDatabase extends RoomDatabase{

    public abstract RSSItemDao rssItemDao();

    private static volatile RSSDatabase INSTANCE;

    public static RSSDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RSSDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RSSDatabase.class, "rss_database")
                            .addCallback(rssDatabaseCallback)
//                            .allowMainThreadQueries() //fixme: may not be in production
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback rssDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);

            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RSSItemDao itemDao;

        PopulateDbAsync(RSSDatabase db) {
            itemDao = db.rssItemDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            itemDao.deleteAll();

//            RSSItem item = new RSSItem("title", "link", "img", "desc",
//                    OffsetDateTimeToStringConverter.getDateFromString("Thu, 03 Mar 2016 15:30:00 +0200"));
//            item.id = itemDao.insert(item);
            return null;
        }

    }
}
