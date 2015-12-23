package movies.swapover.de.movies.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mikulicv on 15.12.15.
 */
public class MovieDb {


    private static final String LOG_TAG = "MovieDb";
    public static final String SQLITE_TABLE = "Movie";


    public static final String ROW_ID = "_id";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ORIG_TITLE = "orig_title";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_BACKDROP = "backdrop";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    ROW_ID + " integer PRIMARY KEY autoincrement," +
                    COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_ORIG_TITLE + " TEXT, " +
                    COLUMN_POSTER + " TEXT, " +
                    COLUMN_BACKDROP + " TEXT, " +
                    COLUMN_OVERVIEW + " TEXT, " +
                    COLUMN_POPULARITY + " TEXT, " +
                    COLUMN_RATING + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    " UNIQUE (" + COLUMN_MOVIE_ID + "));";

    public static void onCreate(SQLiteDatabase db){
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE ID EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }


}
