package ro.basilescu.bogdan.templateapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.basilescu.bogdan.templateapplication.R;
import ro.basilescu.bogdan.templateapplication.TemplateApplication;
import ro.basilescu.bogdan.templateapplication.adapters.decorators.DividerItemDecoration;
import ro.basilescu.bogdan.templateapplication.rest.FileClient;
import ro.basilescu.bogdan.templateapplication.rest.MovieApiClient;
import ro.basilescu.bogdan.templateapplication.rest.MovieApiInterface;
import ro.basilescu.bogdan.templateapplication.restmodel.Movie;
import ro.basilescu.bogdan.templateapplication.restmodel.MovieResponse;
import ro.basilescu.bogdan.templateapplication.utils.FileUtils;

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

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getActivity(), fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getActivity().getContentResolver().getType(fileUri)),
                file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void uploadFile(Uri fileUri) {
        String name = "Name";
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, name);

        RequestBody filePart = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)),
                FileUtils.getFile(getActivity(), fileUri));
        MultipartBody.Part file = MultipartBody.Part.createFormData("photo", new File("filepath").getName(), filePart);
        // get retrofit instance
        // call uploadPhoto method from interface FileClient
        // set parameters descriptionPart and file for the method and check the response on the server
    }

    private void uploadFiles(Uri fileUriProfile, Uri fileUriPanorama) {
        //create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("baseUrl here")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        //get client and call object for the request
        FileClient fileClient = retrofit.create(FileClient.class);

        // finally, execute the request
        Call<ResponseBody> call = fileClient.uploadPhotos(
                prepareFilePart("profile", fileUriProfile),
                prepareFilePart("panorama", fileUriPanorama));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void uploadAlbum(List<Uri> fileUris) {
        String description = "description";

        //create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("baseUrl")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        // get client & call object for the request
        FileClient fileClient = retrofit.create(FileClient.class);

        List<MultipartBody.Part> parts = new ArrayList<>();

        for (int i = 0; i < fileUris.size(); i++) {
            parts.add(prepareFilePart("" + i, fileUris.get(i)));
        }

        // finally, execute the request
        Call<ResponseBody> call = fileClient.uploadAlbum(createPartFromString(description), parts);
    }

    private void uploadFileWithMultiPartBody(Uri fileUri) {
        String description = "description";
        String photographer = "photographer";
        String year = "year";
        String location = "location";

        // create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("baseUrl")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        // get client & call object for the request
        FileClient fileClient = retrofit.create(FileClient.class);

        // finally, execute the request
        Call<ResponseBody> call = fileClient.uploadPhotoWithMultipartBodyPart(createPartFromString(description),
                createPartFromString(location),
                createPartFromString(photographer),
                createPartFromString(year),
                prepareFilePart("photo", fileUri));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void uploadFileWithPartMap(Uri fileUri) {
        String description = "description";
        String photographer = "photographer";
        String year = "year";
        String location = "location";

        // create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("baseUrl")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        // get client & call object for the request
        FileClient fileClient = retrofit.create(FileClient.class);

        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("client", createPartFromString("android"));
        partMap.put("secret", createPartFromString("theplaymaker"));
        if (!TextUtils.isEmpty(description)) {
            partMap.put("description", createPartFromString(description));
        }
        if (!TextUtils.isEmpty(photographer)) {
            partMap.put("photographer", createPartFromString(photographer));
        }
        if (!TextUtils.isEmpty(year)) {
            partMap.put("year", createPartFromString(year));
        }
        if (!TextUtils.isEmpty(location)) {
            partMap.put("location", createPartFromString(location));
        }
        // finally, execute the request
        Call<ResponseBody> call = fileClient.uploadPhotoWithPartMap(partMap,
                prepareFilePart("photo", fileUri));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle response
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void downloadFile(String fileUrl) {
        // Create Retrofit builder
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("baseUrl");
        // Get Retrofit instance based on builder
        Retrofit retrofit = builder.build();
        // Create Retrofit file service
        FileClient fileDownloadClient = retrofit.create(FileClient.class);

        Call<ResponseBody> call = fileDownloadClient.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File file = new File(Arrays.toString(getActivity().getExternalFilesDirs("FileDirectory")) + File.separator + "Future Studio Icon.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
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
