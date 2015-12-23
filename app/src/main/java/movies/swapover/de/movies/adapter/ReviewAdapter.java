package movies.swapover.de.movies.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import movies.swapover.de.movies.R;
import movies.swapover.de.movies.model.Review;

/**
 * Created by mikulicv on 09.12.15.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    Context ctx;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.review_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

            final Review review = getItem(position);

            viewHolder = (ViewHolder) view.getTag();

            viewHolder.authorView.setText(review.getAuthor());
            viewHolder.contentView.setText(Html.fromHtml(review.getContent()));

            return view;
    }

    public static class ViewHolder {
        public final TextView authorView;
        public final TextView contentView;

        public ViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
        }
    }
}
