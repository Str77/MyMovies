package com.example.mymovies.utils;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static final String RESULT_KEY = "results";

    private static final String YOUTUBE_KEY = "https://www.youtube.com/watch?v=";
    private static final String TRAILER_KEY_KEY = "key";
    private static final String TRAILER_NAME_KEY = "name";

    private static final String REVIEW_AUTHOR_KEY = "author";
    private static final String REVIEW_CONTENT_KEY = "content";

    private static final String ID_KEY = "id";
    private static final String VOTE_COUNT_KEY = "vote_count";
    private static final String TITLE_KEY = "title";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String OVERVIEW_KEY = "overview";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String BACKDROP_KEY = "backdrop_path";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String RELEASE_DATE_KEY = "release_date";

    private static final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
    private static final String BIG_POSTER_SIZE = "w780";

    public static ArrayList<Movie> getMoviesOfJSON(JSONObject jsonObject) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (jsonObject == null) {
            return movies;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(RESULT_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectFromArray = jsonArray.getJSONObject(i);
                int id = jsonObjectFromArray.getInt(ID_KEY);
                int voteCount = jsonObjectFromArray.getInt(VOTE_COUNT_KEY);
                String title = jsonObjectFromArray.getString(TITLE_KEY);
                String originalTitle = jsonObjectFromArray.getString(ORIGINAL_TITLE_KEY);
                String overview = jsonObjectFromArray.getString(OVERVIEW_KEY);
                String posterPath = IMAGE_BASE_PATH + SMALL_POSTER_SIZE + jsonObjectFromArray.getString(POSTER_PATH_KEY);
                String bigPosterPath = IMAGE_BASE_PATH + BIG_POSTER_SIZE + jsonObjectFromArray.getString(POSTER_PATH_KEY);
                String backdropPath = jsonObjectFromArray.getString(BACKDROP_KEY);
                double voteAverage = jsonObjectFromArray.getDouble(VOTE_AVERAGE_KEY);
                String releaseDate = jsonObjectFromArray.getString(RELEASE_DATE_KEY);
                Movie movie = new Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate);
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static ArrayList<Trailer> getTrailersOfJSON(int movieId, String lang) {
        JSONObject jsonObject = NetworkUtils.getTrailerJSON(movieId, lang);
        ArrayList<Trailer> trailers = new ArrayList<>();
        if (jsonObject == null) {
            return trailers;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(RESULT_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectTrailer = jsonArray.getJSONObject(i);
                String name = jsonObjectTrailer.getString(TRAILER_NAME_KEY);
                String url = YOUTUBE_KEY + jsonObjectTrailer.getString(TRAILER_KEY_KEY);
                Trailer trailer = new Trailer(url, name);
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<Review> getReviewsOfJSON(int movieId) {
        JSONObject jsonObject = NetworkUtils.getReviewJSON(movieId);
        ArrayList<Review> reviews = new ArrayList<>();
        if (jsonObject == null) {
            return reviews;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(RESULT_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectTrailer = jsonArray.getJSONObject(i);
                String author = jsonObjectTrailer.getString(REVIEW_AUTHOR_KEY);
                String content = jsonObjectTrailer.getString(REVIEW_CONTENT_KEY);
                Review review = new Review(author, content);
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
