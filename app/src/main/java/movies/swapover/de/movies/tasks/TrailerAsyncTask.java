package movies.swapover.de.movies.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import movies.swapover.de.movies.DetailActivityFragment;
import movies.swapover.de.movies.adapter.TrailerAdapter;
import movies.swapover.de.movies.model.Trailer;
import movies.swapover.de.movies.util.MovieJSONLoader;

/**
 * Created by mikulicv on 22.12.15.
 */
public class TrailerAsyncTask extends AsyncTask<Object, Void, List<Trailer>> {
    DetailActivityFragment caller;
    Context ctx;

    public TrailerAsyncTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected List<Trailer> doInBackground(Object... params) {

        if (params.length == 0) {
            return null;
        }

        caller = (DetailActivityFragment) params[1];

        final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";

        String json = new MovieJSONLoader(ctx).load(BASE_URL,null);

        return parseTrailerFromJson(json);
    }

    private List<Trailer> parseTrailerFromJson(String json)  {
        JsonParser parser = new JsonParser();
        JsonArray jsonObject = (JsonArray) parser.parse(json).getAsJsonObject().get("results");
        return new Gson().fromJson(jsonObject, new TypeToken<List<Trailer>>() {}.getType());

    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if (trailers != null) {
            if (trailers.size() > 0) {
                caller.getTrailersCardview().setVisibility(View.VISIBLE);
                TrailerAdapter adapter = caller.getTrailerAdapter();
                if (adapter != null) {
                    adapter.clear();
                    for (Trailer trailer : trailers) {
                        adapter.add(trailer);
                    }
                }

                caller.setSharedTrailer(trailers.get(0));
                ShareActionProvider provider = caller.getShareActionProvider();
                if (provider != null) {
                    provider.setShareIntent(caller.createShareMovieIntent());
                }
            }
        }
    }
}
