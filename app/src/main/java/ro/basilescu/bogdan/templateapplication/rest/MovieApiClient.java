package ro.basilescu.bogdan.templateapplication.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class for creating the retrofit instance by using Singleton Pattern
 * Using RetrofitBuilder class to construct network requests to the MovieDB Api
 * Adding HttpLoggingInterceptor to intercept the HttpResponse and display it in logs
 */
public class MovieApiClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit sRetrofit = null;

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            Retrofit.Builder sBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            sHttpClient.addInterceptor(interceptor);
            sRetrofit = sBuilder.client(sHttpClient.build()).build();
        }
        return sRetrofit;
    }
}
