package ro.basilescu.bogdan.templateapplication.restmodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Movie mapped for Json Schema for REST service
 */
public class Movie {
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("adult")
    private boolean mAdult;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("genre_ids")
    private List<Integer> mGenreIds = new ArrayList<>();
    @SerializedName("id")
    private int mId;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("popularity")
    private double mPopularity;
    @SerializedName("vote_count")
    private int mVoteCount;
    @SerializedName("video")
    private boolean mVideo;
    @SerializedName("vote_average")
    private double mVoteAverage;

    public Movie(String posterPath, boolean adult, String overview,
                 String releaseDate, List<Integer> genreIds, int id,
                 String originalTitle, String originalLanguage, String title,
                 String backdropPath, double popularity, int voteCount,
                 boolean video, double voteAverage) {
        mPosterPath = posterPath;
        mAdult = adult;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mGenreIds = genreIds;
        mId = id;
        mOriginalTitle = originalTitle;
        mOriginalLanguage = originalLanguage;
        mTitle = title;
        mBackdropPath = backdropPath;
        mPopularity = popularity;
        mVoteCount = voteCount;
        mVideo = video;
        mVoteAverage = voteAverage;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean adult) {
        mAdult = adult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return mGenreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        mGenreIds = genreIds;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguage = originalLanguage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean video) {
        mVideo = video;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }
}
