package movies.swapover.de.movies.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import movies.swapover.de.movies.database.FavoriteContentProvider;
import movies.swapover.de.movies.database.MovieDb;

/**
 * Created by mikulicv on 22.12.15.
 */
public class MovieUtil {

    public static Boolean isFavoriteMovie(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
        boolean isFavorite = false;
        if (cursor != null) {
            int index = cursor.getColumnIndexOrThrow(MovieDb.COLUMN_MOVIE_ID);
            while (cursor.moveToNext()) {
                int movieID = cursor.getInt(index);
                Log.d("Movie DB", "Movie ID " + movieID + " selectedMovie ID " + id);
                if(id == movieID){
                    isFavorite = true;
                    break;
                }
            }
        }
        return isFavorite;
    }

    public static String getFormattedDate(String release_date) {
        SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatOutput = new SimpleDateFormat("MM/dd/yy");
        String formattedDate ="";
        try {
            Date date = formatInput.parse(release_date);
            formattedDate = formatOutput.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}
