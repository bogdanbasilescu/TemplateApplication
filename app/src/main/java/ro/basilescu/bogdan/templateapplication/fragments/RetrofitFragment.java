package ro.basilescu.bogdan.templateapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.basilescu.bogdan.templateapplication.R;
import ro.basilescu.bogdan.templateapplication.TemplateApplication;
import ro.basilescu.bogdan.templateapplication.adapters.decorators.DividerItemDecoration;
import ro.basilescu.bogdan.templateapplication.rest.MovieApiInterface;
import ro.basilescu.bogdan.templateapplication.rest.MovieApiClient;
import ro.basilescu.bogdan.templateapplication.restmodel.Movie;
import ro.basilescu.bogdan.templateapplication.restmodel.MovieResponse;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentRetrofitListener} interface
 * to handle interaction events.
 * Use the {@link RetrofitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RetrofitFragment extends Fragment {

    private static final String TAG = "RetrofitFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentRetrofitListener mListener;

    public RetrofitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RetrofitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RetrofitFragment newInstance(String param1, String param2) {
        RetrofitFragment fragment = new RetrofitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this method to create a basic new instance
     * of this fragment
     *
     * @return A new instance of fragment FirebaseFragment
     */
    public static RetrofitFragment newInstance() {
        return new RetrofitFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentRetrofitListener) {
            mListener = (OnFragmentRetrofitListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentFirebaseListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrofit, container, false);

        /*
         * Define recyclerView and add Divider for the list using ItemDecorator
         */
        final RecyclerView recyclerViewRetrofit = (RecyclerView) view.findViewById(R.id.recycler_view_retrofit);
        recyclerViewRetrofit.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewRetrofit.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        /*
         *  Create Retrofit service
         *  Call method to get top rated movies
         */
        MovieApiInterface movieApiInterface = MovieApiClient.getInstance().create(MovieApiInterface.class);
        Call<MovieResponse> call = movieApiInterface.getTopRatedMovies(TemplateApplication.MOVIE_DB_API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                // Retrieve list of movies as response
                List<Movie> movies = response.body().getResults();
                // Set the retrieved list for the adapter and set the adapter for the recyclerView
                recyclerViewRetrofit.setAdapter(new MoviesAdapter(movies, R.layout.item_movie, getActivity()));
                // Log for debug to check the received status code and the size of the list
                Log.d(TAG, "Status code: " + statusCode);
                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentRetrofitListener {
        // TODO: Update argument type and name
        void onFragmentRetrofitInteraction(String retrofitEvent);
    }

    /**
     * RecyclerView Adapter class for Movies
     */
    private class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

        private List<Movie> movies;
        private int rowLayout;
        private Context context;

        /**
         * Constructor
         *
         * @param movies    The list of movies that needs to be displayed
         * @param rowLayout The layout of an item from the adapter
         * @param context   The context of the Adapter
         */
        public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
            this.movies = movies;
            this.rowLayout = rowLayout;
            this.context = context;
        }

        /**
         * Returns the ViewHolder to use for this adapter.
         *
         * @param parent   The ViewGroup
         * @param viewType The viewType of the view
         * @return The ViewHolder for the Adapter
         */
        @Override
        public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
            return new MovieViewHolder(view);
        }

        /**
         * Binds the view to the items of the Adapter
         *
         * @param holder   The ViewHolder for the Adapter
         * @param position The position to which is pointing in the Adapter
         */
        @Override
        public void onBindViewHolder(MovieViewHolder holder, final int position) {
            holder.movieTitle.setText(movies.get(position).getTitle());
            holder.data.setText(movies.get(position).getReleaseDate());
            holder.movieDescription.setText(movies.get(position).getOverview());
            holder.rating.setText(String.valueOf(movies.get(position).getVoteAverage()));
        }

        /**
         * Get the total number of items of the Adapter
         *
         * @return the size of the list of the Adapter
         */
        @Override
        public int getItemCount() {
            return movies.size();
        }

        /**
         * MovieViewHolder class to display the Movie - ViewHolder pattern
         */
        public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            LinearLayout moviesLayout;
            TextView movieTitle;
            TextView data;
            TextView movieDescription;
            TextView rating;

            public MovieViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                moviesLayout = (LinearLayout) view.findViewById(R.id.movies_layout);
                movieTitle = (TextView) view.findViewById(R.id.title);
                data = (TextView) view.findViewById(R.id.subtitle);
                movieDescription = (TextView) view.findViewById(R.id.description);
                rating = (TextView) view.findViewById(R.id.rating);
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Item clicked!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
