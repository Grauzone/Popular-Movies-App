package movies.swapover.de.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import movies.swapover.de.movies.R;
import movies.swapover.de.movies.model.Movie;


/**
 * Created by mikulicv on 07.12.15.
 */
public class GridMovieAdapter extends ArrayAdapter<Movie> {

    private Context ctx;

    public GridMovieAdapter(Context context, List<Movie> objects) {
        super(context, 0, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item_movie, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Movie movie = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        String movieImage = movie.getPoster_path();
        Picasso.with(ctx).load("http://image.tmdb.org/t/p/w185/" + movieImage).into(viewHolder.imageView);

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.picture);
        }
    }






}
