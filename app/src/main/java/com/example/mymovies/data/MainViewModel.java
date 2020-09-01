package com.example.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MoviesDatabase database;

    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
        favouriteMovies = database.moviesDao().getAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public Movie getMovieById(int id) {
        try {
            return new getMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavouriteMovie getFavouriteMovieById(int id) {
        try {
            return new getFavouriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deteleAllMovies() {
        new deleteAllMovieTask().execute();
    }

    public void deleteAllFavouriteMovies() {
        new deleteAllFavouriteMovieByIdTask().execute();
    }

    public void insertMovie(Movie movie) {
        new insertMovieTask().execute(movie);
    }

    public void insertFavouriteMovie(FavouriteMovie favouriteMovie) {
        new insertFavouriteMovieTask().execute(favouriteMovie);
    }

    public void deleteMovie(Movie movie) {
        new deleteMovieTask().execute(movie);
    }

    public void deleteFavouriteMovie(FavouriteMovie favouriteMovie) {
        new deleteFavouriteMovieTask().execute(favouriteMovie);
    }

    private static class getMovieByIdTask extends AsyncTask<Integer, Void, Movie> {
        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers.length > 0 && integers != null) {
                return database.moviesDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class getFavouriteMovieByIdTask extends AsyncTask<Integer, Void, FavouriteMovie> {
        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if (integers.length > 0 && integers != null) {
                return database.moviesDao().getFavoriteMovieId(integers[0]);
            }
            return null;
        }
    }

    private static class deleteAllMovieTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... integers) {
            database.moviesDao().deleteAllMovies();
            return null;
        }
    }

    private static class deleteAllFavouriteMovieByIdTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... integers) {
            database.moviesDao().deleteAllFavouriteMovies();
            return null;
        }
    }

    private static class insertMovieTask extends AsyncTask<Movie, Void, Void> {
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies.length > 0 && movies != null) {
                database.moviesDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    private static class insertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies.length > 0 && movies != null) {
                database.moviesDao().insertFavouriteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class deleteMovieTask extends AsyncTask<Movie, Void, Void> {
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies.length > 0 && movies != null) {
                database.moviesDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class deleteFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies.length > 0 && movies != null) {
                database.moviesDao().deleteFavouriteMovie(movies[0]);
            }
            return null;
        }
    }
}
