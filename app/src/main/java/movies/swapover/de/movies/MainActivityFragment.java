package movies.swapover.de.movies;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import movies.swapover.de.movies.adapter.GridMovieAdapter;
import movies.swapover.de.movies.model.Movie;
import movies.swapover.de.movies.settings.SettingsFragment;
import movies.swapover.de.movies.tasks.FavoriteAsyncTask;
import movies.swapover.de.movies.tasks.MovieAsyncTask;
import movies.swapover.de.movies.util.SortOrder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String MOVIES_KEY = "movies";
    private GridView gridView;
    private GridMovieAdapter gridMovieAdapter;
    private ArrayList<Movie> movies = null;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = getSortOrder(sharedPref.getString(SettingsFragment.KEY_PREF_SORT_ORDER, ""));

       View view = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) view.findViewById(R.id.gridview);

        gridMovieAdapter = new GridMovieAdapter(getActivity(), new ArrayList<Movie>());

        gridView.setAdapter(gridMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = gridMovieAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                gridMovieAdapter.addAll(movies);
            } else {
                updateMovies(sortOrder);
            }
        } else {
            updateMovies(sortOrder);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(movies != null){
            outState.putParcelableArrayList(MOVIES_KEY, movies);
        }
        super.onSaveInstanceState(outState);
    }


    private void updateMovies(String sort_by){
        if(!sort_by.equals(SortOrder.FAVORITE)){
            new MovieAsyncTask(getContext()).execute(sort_by,this);
        }else{
            new FavoriteAsyncTask(getContext()).execute(this);
        }
    }

    private String getSortOrder(String string) {
        if(string.equals("by favorite")){
            return SortOrder.FAVORITE;
        }else if(string.equals("highest rated")){
            return SortOrder.RATING_DESC;
        }else{
            return SortOrder.POPULARITY_DESC;
        }

    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }

    public GridMovieAdapter getGridMovieAdapter() {
        return gridMovieAdapter;
    }



}
