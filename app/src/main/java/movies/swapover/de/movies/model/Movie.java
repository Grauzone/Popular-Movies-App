package movies.swapover.de.movies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import movies.swapover.de.movies.database.MovieDb;

/**
 * Created by mikulicv on 07.12.15.
 */
public final class Movie implements Parcelable{
    private boolean adult;
    private String backdrop_path;
    private List<Integer> genre_ids;
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private Float popularity;
    private String title;
    private boolean video;
    private Float vote_average;
    private int vote_count;

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_MOVIE_ID));
        this.title = cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_TITLE));
        this.original_title = cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_ORIG_TITLE));
        this.poster_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_POSTER));
        this.backdrop_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_BACKDROP));
        this.overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_OVERVIEW));
        this.popularity = Float.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_POPULARITY)));
        this.vote_average = Float.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_RATING)));
        this.release_date = cursor.getString(cursor.getColumnIndexOrThrow(MovieDb.COLUMN_DATE));
    }


    protected Movie(Parcel in) {
        adult = in.readByte() != 0;
        backdrop_path = in.readString();
        genre_ids = new ArrayList<Integer>();
        in.readList(genre_ids,null);
        id = in.readInt();
        original_language = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        popularity = in.readFloat();
        title = in.readString();
        video = in.readByte() != 0;
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(backdrop_path);
        dest.writeList(genre_ids);
        dest.writeInt(id);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeFloat(popularity);
        dest.writeString(title);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeFloat(vote_average);
        dest.writeInt(vote_count);

    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Float getVote_average() {
        return vote_average;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
