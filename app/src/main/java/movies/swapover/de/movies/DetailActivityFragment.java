package movies.swapover.de.movies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.swapover.de.movies.adapter.ReviewAdapter;
import movies.swapover.de.movies.adapter.TrailerAdapter;
import movies.swapover.de.movies.database.FavoriteContentProvider;
import movies.swapover.de.movies.database.MovieDb;
import movies.swapover.de.movies.model.Movie;
import movies.swapover.de.movies.model.Review;
import movies.swapover.de.movies.model.Trailer;
import movies.swapover.de.movies.tasks.ReviewAsyncTask;
import movies.swapover.de.movies.tasks.TrailerAsyncTask;
import movies.swapover.de.movies.util.MovieUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    private Movie selectedMovie;
    private Trailer sharedTrailer;
    private ShareActionProvider shareActionProvider;
    private Toast toast;

    private CardView reviewsCardview;
    private CardView trailersCardview;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        if (selectedMovie != null) {

            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            MenuItem action_share = menu.findItem(R.id.action_share);

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    return MovieUtil.isFavoriteMovie(getActivity(), selectedMovie.getId());
                }

                @Override
                protected void onPostExecute(Boolean isFavorited) {
                    action_favorite.setIcon(isFavorited ?
                            R.drawable.abc_btn_rating_star_on_mtrl_alpha :
                            R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();

            shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            if (sharedTrailer != null) {
                shareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                if (selectedMovie != null) {
                    new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            return MovieUtil.isFavoriteMovie(getActivity(), selectedMovie.getId());
                        }

                        @Override
                        protected void onPostExecute(Boolean isFavorited) {
                            if (isFavorited) {
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        Uri uri = Uri.parse(FavoriteContentProvider.CONTENT_URI + "/" + selectedMovie.getId());
                                        return getActivity().getApplicationContext().getContentResolver().delete(uri, null, null);
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (toast != null) {
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            } else {
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();
                                        values.put(MovieDb.COLUMN_MOVIE_ID, selectedMovie.getId());
                                        values.put(MovieDb.COLUMN_POSTER, selectedMovie.getPoster_path());
                                        values.put(MovieDb.COLUMN_BACKDROP, selectedMovie.getBackdrop_path());
                                        values.put(MovieDb.COLUMN_DATE, selectedMovie.getRelease_date());
                                        values.put(MovieDb.COLUMN_OVERVIEW, selectedMovie.getOverview());
                                        values.put(MovieDb.COLUMN_POPULARITY, selectedMovie.getPopularity());
                                        values.put(MovieDb.COLUMN_RATING, selectedMovie.getVote_average());
                                        values.put(MovieDb.COLUMN_TITLE, selectedMovie.getTitle());
                                        values.put(MovieDb.COLUMN_ORIG_TITLE, selectedMovie.getOriginal_title());
                                        return getContext().getApplicationContext().getContentResolver().insert(FavoriteContentProvider.CONTENT_URI, values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (toast != null) {
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            selectedMovie = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ScrollView mDetailLayout = (ScrollView) view.findViewById(R.id.detail_layout);

        if (selectedMovie != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
            addContent(view);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }


        reviewsCardview = (CardView) view.findViewById(R.id.detail_reviews_cardview);
        trailersCardview = (CardView) view.findViewById(R.id.detail_trailers_cardview);

        LinearListView trailersView = (LinearListView) view.findViewById(R.id.detail_trailers);
        LinearListView reviewsView = (LinearListView) view.findViewById(R.id.detail_reviews);

        trailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        trailersView.setAdapter(trailerAdapter);

        trailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                Trailer trailer = trailerAdapter.getItem(position);
                startYoutubeVideo(trailer.getKey());
            }
        });

        reviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
        reviewsView.setAdapter(reviewAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (selectedMovie != null) {
            new TrailerAsyncTask(getContext()).execute(selectedMovie.getId(), this);
            new ReviewAsyncTask(getContext()).execute(selectedMovie.getId(), this);
        }
    }

    private void startYoutubeVideo(String key) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            intent.putExtra("force_fullscreen", true);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
            getContext().startActivity(intent);
        }
    }


    public Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool movie: " + selectedMovie.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + sharedTrailer.getKey());
        return shareIntent;
    }

    private void addContent(View view) {

        TextView detailHeadline = (TextView) view.findViewById(R.id.datailHeadline);
        detailHeadline.setText(selectedMovie.getOriginal_title());

        TextView date = (TextView) view.findViewById(R.id.detailDate);
        date.setText(MovieUtil.getFormattedDate(selectedMovie.getRelease_date()));

        TextView averageVote = (TextView) view.findViewById(R.id.detailVote);
        averageVote.setText(selectedMovie.getVote_average().toString());

        TextView detailSynopsis = (TextView) view.findViewById(R.id.detailSynopsis);
        detailSynopsis.setText(selectedMovie.getOverview());

        ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/" + selectedMovie.getBackdrop_path()).into(backdrop);
    }

    public CardView getTrailersCardview() {
        return trailersCardview;
    }

    public TrailerAdapter getTrailerAdapter() {
        return trailerAdapter;
    }

    public void setSharedTrailer(Trailer sharedTrailer) {
        this.sharedTrailer = sharedTrailer;
    }

    public ShareActionProvider getShareActionProvider() {
        return shareActionProvider;
    }

    public CardView getReviewsCardview() {
        return reviewsCardview;
    }

    public ReviewAdapter getReviewAdapter() {
        return reviewAdapter;
    }
}
