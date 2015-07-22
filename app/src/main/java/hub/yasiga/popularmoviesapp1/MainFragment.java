package hub.yasiga.popularmoviesapp1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.MoviePOJO;
import model.Result;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class MainFragment extends Fragment {

    private static final String SORT_BY = "popularity.desc";

    private static final String API_KEY = "232c7933fb923517762fbaba80f80ba9";

    private SharedPreferences mSharedPreferences;


    GridView gridView;

    private PopularMoviesStreamAdapter mAdapter;

    public ProgressBar mProgressBar;

    private List<Result> result = new ArrayList<Result>();

    private final String MOVIES_KEY="result";

    private Bundle bundle;

    private boolean mIsDownloadInProgress = false;

    private String QUERY_TYPE;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            ArrayList<Result> result_Saved = savedInstanceState.getParcelableArrayList(MOVIES_KEY);

            mAdapter = new PopularMoviesStreamAdapter(getActivity());
            mAdapter.setData(result_Saved);
        }
        else {
            downloadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_main, container, false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrderKey = getResources().getString(R.string.pref_sort_order_key);
        QUERY_TYPE = mSharedPreferences.getString(sortOrderKey,"");
        Log.i("MainActvivity","Sorty type: "+QUERY_TYPE);


        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        gridView = (GridView) view.findViewById(R.id.grid);

        mAdapter = new PopularMoviesStreamAdapter(getActivity());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Result result = (Result)mAdapter.getItem(i);

                Intent intent = new Intent(getActivity(), MovieDetails.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("result", result);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        downloadData();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<Result>) result);
    }


    //Checking for Connection so we can inflater the Layout and Handling the force Stop caused by no Internet Connection
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInterface = connectivityManager.getActiveNetworkInfo();
        return networkInterface != null;
    }

    public void downloadData(){

        if (!mIsDownloadInProgress){

            mIsDownloadInProgress = true;

            mProgressBar.setVisibility(View.VISIBLE);



            MovieClient.getPopularMoviesClient().getStreams(QUERY_TYPE, API_KEY, new Callback<MoviePOJO>() {
                @Override
                public void success(MoviePOJO moviePOJO, Response response) {
                    consumeApiData(moviePOJO);
                }

                @Override
                public void failure(RetrofitError error) {
                    //consumeApiData(null);

                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.e("ErrorTag", error.toString());

                }
            });
        }
    }


    public void consumeApiData(MoviePOJO moviePOJO) {
        if (moviePOJO != null) {

            mAdapter = new PopularMoviesStreamAdapter(getActivity());

            Log.i("MAinActivity","Movie response: "+moviePOJO.getResults().size()+" and titile is: "+moviePOJO.getResults().get(0).getTitle()+" And poster is: "+moviePOJO.getResults().get(0).getPoster_path());


            mAdapter.setData(moviePOJO.getResults());

            gridView.setAdapter(mAdapter);

            // Done loading; remove loading indicator
            mProgressBar.setVisibility(View.GONE);

            // Keep track of what page to download next
            //mState.nextPage++;
        }

        mIsDownloadInProgress = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        downloadData();
    }






}
