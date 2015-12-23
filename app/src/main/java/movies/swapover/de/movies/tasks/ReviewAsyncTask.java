package movies.swapover.de.movies.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import movies.swapover.de.movies.DetailActivityFragment;
import movies.swapover.de.movies.adapter.ReviewAdapter;
import movies.swapover.de.movies.model.Review;
import movies.swapover.de.movies.util.MovieJSONLoader;

/**
 * Created by mikulicv on 22.12.15.
 */
public class ReviewAsyncTask extends AsyncTask<Object, Void, List<Review>> {

    private Context ctx;
    DetailActivityFragment caller;

    public ReviewAsyncTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected List<Review> doInBackground(Object... params) {

        if (params.length == 0) {
            return null;
        }

        caller = (DetailActivityFragment) params[1];

        final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";

        String json = new MovieJSONLoader(ctx).load(BASE_URL,null);

        return parseReviewsFromJson(json);
    }

    private List<Review> parseReviewsFromJson(String json) {

        JsonParser parser = new JsonParser();
        JsonArray jsonObject = (JsonArray) parser.parse(json).getAsJsonObject().get("results");
        return new Gson().fromJson(jsonObject, new TypeToken<List<Review>>() {}.getType());
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (reviews != null) {
            if (reviews.size() > 0) {
                caller.getReviewsCardview().setVisibility(View.VISIBLE);
                ReviewAdapter adapter = caller.getReviewAdapter();
                if (adapter != null) {
                    adapter.clear();
                    for (Review review : reviews) {
                        adapter.add(review);
                    }
                }
            }
        }
    }
}
