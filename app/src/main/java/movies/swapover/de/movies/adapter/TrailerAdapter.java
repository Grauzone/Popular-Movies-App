package movies.swapover.de.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.swapover.de.movies.R;
import movies.swapover.de.movies.model.Trailer;


/**
 * Created by mikulicv on 09.12.15.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    Context ctx;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        super(context, 0, trailers);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        final Trailer trailer = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        String thumbnail_url = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Picasso.with(ctx).load(thumbnail_url).into(viewHolder.imageView);

        viewHolder.nameView.setText(trailer.getName());

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView nameView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }

}
