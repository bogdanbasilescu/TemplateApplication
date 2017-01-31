package ro.basilescu.bogdan.templateapplication;

import android.app.Application;

public class TemplateApplication extends Application {
    /**
     * Todo: Define application constants here
     */
    // API key for Retrofit for Movie DB
    public static final String MOVIE_DB_API_KEY = "3ae570adc23bfdb0e0d111a8cc8c1621";

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * Todo: Initialize configuration here
         */
    }
}
