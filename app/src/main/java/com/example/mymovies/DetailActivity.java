package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapters.ReviewsAdapter;
import com.example.mymovies.adapters.TrailersAdapter;
import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;
import com.example.mymovies.utils.JSONUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private ScrollView scrollViewInfo;
    private ImageView imageViewFavourite;
    private MainViewModel viewModel;
    private Movie movie;
    private FavouriteMovie favouriteMovie;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private TrailersAdapter trailersAdapter = new TrailersAdapter();
    private ReviewsAdapter reviewsAdapter = new ReviewsAdapter();
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private TextView textViewComments;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mainMenu:
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
                break;
            case R.id.favouriteMenu:
                startActivity(new Intent(DetailActivity.this, FavouriteActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (getIntent().hasExtra("id")) {
        int id = getIntent().getIntExtra("id", -1);
        int method = getIntent().getIntExtra("method", -1);
        switch (method) {
            case 0:
                movie = viewModel.getMovieById(id);
                break;
            case 1:
                movie = viewModel.getFavouriteMovieById(id);
                break;
        }
        } else {
            finish();
        }
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.preview).into(imageViewBigPoster);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewFavourite = findViewById(R.id.imageViewFavourite);
        recyclerViewTrailers = findViewById(R.id.recycleViewTrailers);
        textViewComments = findViewById(R.id.textViewComments);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
        checkOfFavourite();
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        trailers = JSONUtils.getTrailersOfJSON(movie.getId(), Locale.getDefault().getLanguage());
        reviews = JSONUtils.getReviewsOfJSON(movie.getId());
        trailersAdapter.setTrailers(trailers);
        reviewsAdapter.setReviews(reviews);
        recyclerViewTrailers.setAdapter(trailersAdapter);
        textViewComments.setText(String.format(getString(R.string.text_comments), Integer.toString(reviewsAdapter.getReviews().size())));
        recyclerViewReviews.setAdapter(reviewsAdapter);
        trailersAdapter.setOnClickImagePlayIcon(new TrailersAdapter.OnClickImagePlayIcon() {
            @Override
            public void onClickImage(URL url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())));
            }
        });
        scrollViewInfo.smoothScrollTo(0,0);
    }


    public void onClickAddToFavourite(View view) {
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, getString(R.string.toast_text_add_to_favourite), Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, getString(R.string.toast_text_remove_of_favourite), Toast.LENGTH_SHORT).show();
        }
        checkOfFavourite();
    }

    private void checkOfFavourite() {
        favouriteMovie = viewModel.getFavouriteMovieById(movie.getId());
        if (favouriteMovie == null) {
            imageViewFavourite.setImageResource(R.drawable.add);
        } else {
            imageViewFavourite.setImageResource(R.drawable.remove);
        }
    }
}
