package com.example.rssreader.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.OffsetDateTimeConverter;

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

        private final RSSItemDao itemDaoDao;

        PopulateDbAsync(RSSDatabase db) {
            itemDaoDao = db.rssItemDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//        tnDao.deleteAll();
//        mDao.deleteAll();
//        tDao.deleteAll();
//
//        Note note = new Note("title1", "body1");
//        note.id = mDao.insert(note);
//        note = new Note("title2", "body2");
//        note.id = mDao.insert(note);
//        note = new Note("title3", "body3");
//        note.id = mDao.insert(note);
//        note = new Note("title4", "body4");
//        note.id = mDao.insert(note);
//
//        Tag tag = new Tag("tag1");
//        tag.id = tDao.insert(tag);
//        Tag tag2 = new Tag("tag2");
//        tag2.id = tDao.insert(tag2);
//
//        if (note.id == 0 || tag.id == 0) throw new AssertionError();
//
//        tnDao.insert(new TagToNote(tag.id, note.id));
//        tnDao.insert(new TagToNote(tag2.id, note.id));
            return null;
        }
    }
}
