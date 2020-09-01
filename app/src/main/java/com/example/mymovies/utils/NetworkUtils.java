package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String API_KEY = "b918bba3c59ad5f9daf656e75d3e6645";

    private static final String API_KEY_PARAMS = "api_key";
    private static final String LANGUAGE_PARAMS = "language";
    private static final String SORT_BY_PARAMS = "sort_by";
    private static final String PAGE_PARAMS = "page";
    private static final String MIN_VOTE_COUNT_PARAMS = "vote_count.gte";
    private static final String MIN_VOTE_COUNT = "3000";

    private static final String POPULARITY = "popularity.desc";
    private static final String TOP_RATED = "vote_average.desc";

    public static final int POPULARITY_VALUE = 0;
    public static final int TOP_RATED_VALUE = 1;

    private static final String BASE_REVIEW_URL = "https://api.themoviedb.org/3/movie/%s/reviews";
    private static final String BASE_VIDEO_URL = "https://api.themoviedb.org/3/movie/%s/videos";

    private static URL buildTrailerURL(int movieId, String lang) {
        Uri uri = Uri.parse(String.format(BASE_VIDEO_URL, movieId)).buildUpon()
                .appendQueryParameter(API_KEY_PARAMS, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAMS, lang).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL buildReviewURL(int movieId) {
        Uri uri = Uri.parse(String.format(BASE_REVIEW_URL, movieId)).buildUpon()
                .appendQueryParameter(API_KEY_PARAMS, API_KEY).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getReviewJSON(int movieId) {
        try {
            return new BuildJSONObjectTask().execute(buildReviewURL(movieId)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getTrailerJSON(int movieId, String lang) {
        try {
            return new BuildJSONObjectTask().execute(buildTrailerURL(movieId, lang)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildURL(int sortBy, int page, String lang) {
        URL result = null;
        String valueOfSort;
        if (sortBy == POPULARITY_VALUE) {
            valueOfSort = POPULARITY;
        } else {
         valueOfSort = TOP_RATED;
        }
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAMS, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAMS, lang)
                .appendQueryParameter(MIN_VOTE_COUNT_PARAMS, MIN_VOTE_COUNT)
                .appendQueryParameter(SORT_BY_PARAMS, valueOfSort)
                .appendQueryParameter(PAGE_PARAMS, Integer.toString(page))
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSONLoaderTask extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private OnLoadingChangeListener onLoadingChangeListener;

        public interface OnLoadingChangeListener {
            void onLoadChange();
        }

        public void setOnLoadingChangeListener(OnLoadingChangeListener onLoadingChangeListener) {
            this.onLoadingChangeListener = onLoadingChangeListener;
        }

        public JSONLoaderTask(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onLoadingChangeListener != null) {
                onLoadingChangeListener.onLoadChange();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            String urlByBundle = bundle.getString("url");
            if (bundle == null) {
                return null;
            }
            URL url = null;
            try {
                url = new URL(urlByBundle);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            StringBuilder resultForJSON = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    resultForJSON.append(line);
                    line = reader.readLine();
                }
                JSONObject jsonObject = new JSONObject(resultForJSON.toString());
                return jsonObject;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

    private static class BuildJSONObjectTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            if (urls == null && urls.length == 0) {
                return null;
            }
            StringBuilder resultForJSON = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    resultForJSON.append(line);
                    line = reader.readLine();
                }
                JSONObject jsonObject = new JSONObject(resultForJSON.toString());
                return jsonObject;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

    public static JSONObject getMovieJSON(int sortBy, int page, String lang) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new BuildJSONObjectTask().execute(buildURL(sortBy, page, lang)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
