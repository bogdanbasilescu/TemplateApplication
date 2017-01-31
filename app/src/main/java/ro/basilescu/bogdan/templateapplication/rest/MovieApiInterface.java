package ro.basilescu.bogdan.templateapplication.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ro.basilescu.bogdan.templateapplication.restmodel.MovieResponse;

/**
 * Interface for defining endpoints for Movie fetching
 */
public interface MovieApiInterface {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apikey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apikey);
}
