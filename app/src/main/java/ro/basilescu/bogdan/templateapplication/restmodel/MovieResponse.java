package ro.basilescu.bogdan.templateapplication.restmodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class MovieResponse model mapped for Json Schema for REST service
 */
public class MovieResponse {
    @SerializedName("page")
    private int mPage;
    @SerializedName("results")
    private List<Movie> mResults;
    @SerializedName("total_results")
    private int mTotalResults;
    @SerializedName("total_pages")
    private int mTotalPages;

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public List<Movie> getResults() {
        return mResults;
    }

    public void setResults(List<Movie> results) {
        mResults = results;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
    }
}
