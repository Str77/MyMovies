package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.PagerViewHolder> {

    public ViewPager2Adapter(int column) {
        this.movies = new ArrayList<>();
        this.column = column;
    }

    private MoviesAdapter moviesAdapter = new MoviesAdapter();

    private List<Movie> movies;
    private int column;

    public void setMovies(List<Movie> movies) {
        moviesAdapter.setMovies(movies);
    }

    public void addMovies(List<Movie> movies) {
        moviesAdapter.addMovies(movies);
    }

    public void setOnPosterClickListener(MoviesAdapter.OnPosterClickListener onPosterClickListener) {
        moviesAdapter.setOnPosterClickListener(onPosterClickListener);
    }

    public void setOnReachEndListener(MoviesAdapter.OnReachEndListener onReachEndListener) {
        moviesAdapter.setOnReachEndListener(onReachEndListener);
    }

    public List<Movie> getMovies() {
        return moviesAdapter.getMovies();
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false);
        return new PagerViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class PagerViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewMovies;
        private ViewGroup parent;

        public PagerViewHolder(@NonNull final View itemView, ViewGroup parent) {
            super(itemView);
            recyclerViewMovies = itemView.findViewById(R.id.recyclerViewMovies);
            recyclerViewMovies.setLayoutManager(new GridLayoutManager(parent.getContext(), column));
            recyclerViewMovies.setAdapter(moviesAdapter);
        }
    }
}
