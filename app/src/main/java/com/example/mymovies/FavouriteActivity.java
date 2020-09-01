package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymovies.adapters.MoviesAdapter;
import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavourite;
    private MainViewModel viewModel;
    private MoviesAdapter moviesAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.favouriteMenu).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mainMenu) {
            startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private int getColumnCount () {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        recyclerViewFavourite = findViewById(R.id.recyclerViewFavourite);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        moviesAdapter = new MoviesAdapter();
        recyclerViewFavourite.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        recyclerViewFavourite.setAdapter(moviesAdapter);
        LiveData<List<FavouriteMovie>> favouriteMoviesFromDB = viewModel.getFavouriteMovies();
        favouriteMoviesFromDB.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> favouriteMovies) {
                List<Movie> movies = new ArrayList<>();
                if (favouriteMovies != null) {
                    movies.addAll(favouriteMovies);
                    moviesAdapter.setMovies(movies);
                }
            }
        });
        moviesAdapter.setOnPosterClickListener(new MoviesAdapter.OnPosterClickListener() {
            @Override
            public void onClickPoster(int position) {
                Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
                intent.putExtra("id", moviesAdapter.getMovies().get(position).getId());
                intent.putExtra("method", 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
