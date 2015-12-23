package movies.swapover.de.movies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by mikulicv on 15.12.15.
 */
public class FavoriteContentProvider extends ContentProvider {

    private MovieDatabaseHelper dbHelper;

    private static final int ALL_MOVIES = 1;
    private static final int SINGLE_MOVIE = 2;

    private static final String AUTHORITY = "de.swapover.movieapp.movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movie");

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "movie", ALL_MOVIES);
        uriMatcher.addURI(AUTHORITY,"movie/#", SINGLE_MOVIE);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new MovieDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDb.SQLITE_TABLE);

        switch (uriMatcher.match(uri)){
            case ALL_MOVIES:
                //nop
                break;
            case SINGLE_MOVIE:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(MovieDb.COLUMN_MOVIE_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs,null,null,sortOrder);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case ALL_MOVIES:
                return "vnd.android.cursor.dir/vnd.de.swapover.movieapp.movies";
            case SINGLE_MOVIE:
                return "vnd.android.cursor.dir/vnd.de.swapover.movieapp.movies";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_MOVIES:
                //nop
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(MovieDb.SQLITE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_MOVIES:
                //nop
                break;
            case SINGLE_MOVIE:
                String id = uri.getPathSegments().get(1);
                selection = MovieDb.COLUMN_MOVIE_ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(MovieDb.SQLITE_TABLE,selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_MOVIES:
                //nop
                break;
            case SINGLE_MOVIE:
                String id = uri.getPathSegments().get(1);
                selection = MovieDb.COLUMN_MOVIE_ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(MovieDb.SQLITE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return updateCount;
    }
}
