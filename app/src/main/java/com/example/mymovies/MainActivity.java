package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mymovies.adapters.ViewPager2Adapter;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.adapters.MoviesAdapter;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private Switch switchSide;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private ProgressBar progressBarLoading;
    private MainViewModel viewModel;
    private LoaderManager loaderManager;
    private final static int LOADER_ID = 123;
    private static int page = 1;
    private static boolean isLoading = false;
    private ViewPager2 viewPager2;
    private ViewPager2Adapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.mainMenu).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favouriteMenu) {
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class).addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME));
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
        setContentView(R.layout.activity_main);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        viewPager2 = findViewById(R.id.viewPager2Movies);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        adapter = new ViewPager2Adapter(getColumnCount());
        viewPager2.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        loaderManager = LoaderManager.getInstance(this);
        switchSide = findViewById(R.id.switchSide);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        switchSide.setChecked(true);
        switchSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
                downloadContent(getSwitchPosition(isChecked));
            }
        });
        switchSide.setChecked(false);
        LiveData<List<Movie>> moviesFromDB = viewModel.getMovies();
        moviesFromDB.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page == 1) {
                    adapter.setMovies(movies);
                }
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        switchSide.setChecked(false);
                        break;
                    case 1:
                        switchSide.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        adapter.setOnPosterClickListener(new MoviesAdapter.OnPosterClickListener() {
            @Override
            public void onClickPoster(int position) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("id", adapter.getMovies().get(position).getId()).putExtra("method", 0).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        adapter.setOnReachEndListener(new MoviesAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    downloadContent(getSwitchPosition(switchSide.isChecked()));
                }
            }
        });
    }

    public void onClickPopularity(View view) {
        switchSide.setChecked(false);
    }

    public void onClickTopRated(View view) {
        switchSide.setChecked(true);
    }

public int getSwitchPosition(boolean isChecked) {
    int methodOfSort;
    if (isChecked) {
        methodOfSort = NetworkUtils.TOP_RATED_VALUE;
        textViewTopRated.setTextColor(getResources().getColor(R.color.colorAccent));
        textViewPopularity.setTextColor(getResources().getColor(R.color.white));
        viewPager2.setCurrentItem(1);
    } else {
        methodOfSort = NetworkUtils.POPULARITY_VALUE;
        textViewPopularity.setTextColor(getResources().getColor(R.color.colorAccent));
        textViewTopRated.setTextColor(getResources().getColor(R.color.white));
        viewPager2.setCurrentItem(0);
    }
    return methodOfSort;
}

public void downloadContent(int methodOfSort) {
        Bundle bundle = new Bundle();
        URL url = NetworkUtils.buildURL(methodOfSort, page, Locale.getDefault().getLanguage());
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
}

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoaderTask jsonLoaderTask = new NetworkUtils.JSONLoaderTask(this, args);
        jsonLoaderTask.setOnLoadingChangeListener(new NetworkUtils.JSONLoaderTask.OnLoadingChangeListener() {
            @Override
            public void onLoadChange() {
                isLoading = true;
                progressBarLoading.setVisibility(View.VISIBLE);
            }
        });
        return jsonLoaderTask;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesOfJSON(data);
        if (movies.size() > 0 && movies != null) {
                if (page == 1) {
                    viewModel.deteleAllMovies();
                    adapter.getMovies().clear();
                    adapter.notifyDataSetChanged();
                }
                for (Movie movie : movies) {
                    viewModel.insertMovie(movie);
                }
                adapter.addMovies(movies);
            page++;
        }
        progressBarLoading.setVisibility(View.INVISIBLE);
        isLoading = false;
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {
    }
}
