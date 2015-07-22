package hub.yasiga.popularmoviesapp1;

/**
 * Created by user on 7/14/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import model.Result;
import utils.Constants;

/**
 * Created by user on 7/8/2015.
 */
public class MovieDetails extends Activity {

    private Result result;

    //private Toolbar mToolbar;

    private ImageView mMovieThumb;

    private TextView mReleaseDate;

    private TextView mRating;

    private TextView mSynopsis;

    private TextView mOriginalTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movies_details);

        Bundle bundle = getIntent().getExtras();

        //result =(Result) bundle.getSerializable("result");
        result = (Result) bundle.getParcelable("result");

        Log.i("MovieDetails", "result is: " +result.getTitle());

        initFieldsWithData();
    }


    public void initFieldsWithData()
    {
        mMovieThumb = (ImageView)findViewById(R.id.movie_thumb);

        mReleaseDate = (TextView)findViewById(R.id.release_date);

        mRating = (TextView)findViewById(R.id.rating);

        mSynopsis = (TextView)findViewById(R.id.synopsis);

        mOriginalTitle = (TextView)findViewById(R.id.title);

        String imgUrl = Constants.IMAGE_URL_BASE_PATH+result.getPoster_path();

        Picasso.with(this).load(imgUrl).into(mMovieThumb);

        //mOriginalTitle.setText(result.getTitle());
        //mOriginalTitle.setText(result.getOriginal_title());
        mOriginalTitle.setText(result.getTitle());

        mReleaseDate.setText(result.getRelease_date());

        //mReleaseDate.setText(result.getReleaseDate());

        mRating.setText(Float.toString(result.getVote_average()));

        mSynopsis.setText(result.getOverview());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
