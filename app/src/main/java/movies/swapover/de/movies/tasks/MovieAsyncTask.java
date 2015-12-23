package movies.swapover.de.movies.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import movies.swapover.de.movies.adapter.GridMovieAdapter;
import movies.swapover.de.movies.MainActivityFragment;
import movies.swapover.de.movies.model.Movie;
import movies.swapover.de.movies.util.MovieJSONLoader;

/**
 * Created by mikulicv on 21.12.15.
 */
public class MovieAsyncTask extends AsyncTask<Object,Void, List<Movie>> {

    private Context ctx;
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    MainActivityFragment caller;

    public MovieAsyncTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected List<Movie> doInBackground(Object[] params) {

        if (params.length == 0) {
            return null;
        }

        caller = (MainActivityFragment) params[1];

        String sort_by = String.valueOf(params[0]);

        String json = new MovieJSONLoader(ctx).load(BASE_URL,sort_by);

        return parseMoviesFromJson(json);
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

    private List<Movie> parseMoviesFromJson(String json) {
        JsonParser parser = new JsonParser();
        JsonArray jsonObject = (JsonArray) parser.parse(json).getAsJsonObject().get("results");
        return new Gson().fromJson(jsonObject, new TypeToken<List<Movie>>() {}.getType());

    }
}
