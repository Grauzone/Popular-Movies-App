package movies.swapover.de.movies.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import movies.swapover.de.movies.adapter.GridMovieAdapter;
import movies.swapover.de.movies.MainActivityFragment;
import movies.swapover.de.movies.database.FavoriteContentProvider;
import movies.swapover.de.movies.model.Movie;

/**
 * Created by mikulicv on 21.12.15.
 */
public class FavoriteAsyncTask extends AsyncTask<Object, Void, List<Movie>> {

    private Context context;
    MainActivityFragment caller;

    public FavoriteAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Movie> doInBackground(Object[] params) {
        if (params.length == 0) {
            return null;
        }
        caller = (MainActivityFragment) params[0];

        Cursor cursor = context.getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
        return getFavoriteMoviesFromDB(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            GridMovieAdapter adapter = caller.getGridMovieAdapter();
            if (adapter != null) {
                adapter.addAll(movies);
            }
        }
    }

    private List<Movie> getFavoriteMoviesFromDB(Cursor cursor){
        List<Movie> results = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst()){
            do {
                Movie movie = new Movie(cursor);
                results.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }
}
