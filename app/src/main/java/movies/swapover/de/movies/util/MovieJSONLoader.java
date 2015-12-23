package movies.swapover.de.movies.util;

import android.content.Context;
import android.net.Uri;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import movies.swapover.de.movies.R;

/**
 * Created by mikulicv on 22.12.15.
 */
public class MovieJSONLoader {
    Context ctx;
    public MovieJSONLoader(Context ctx) {
        this.ctx = ctx;
    }

    public String load(String base_url, String sort_by){
        HttpURLConnection con = null;
        final String SORT_BY_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";
        try {
            Uri uri = Uri.parse(base_url);
            Uri.Builder builder = uri.buildUpon();
                    if(sort_by != null){
                     builder.appendQueryParameter(SORT_BY_PARAM, sort_by);
                    }
                    builder.appendQueryParameter(API_KEY_PARAM, ctx.getString(R.string.api_key)).build();
            URL url = new URL(builder.build().toString());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            InputStream in = new BufferedInputStream(con.getInputStream());
            String json = IOUtils.toString(in, "UTF-8");

            return json;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return null;
    };

}
