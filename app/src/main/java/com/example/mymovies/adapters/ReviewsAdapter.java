package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder>{

    private ArrayList<Review> reviews;

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.textViewAuthor.setText(reviews.get(position).getAuthor());
        holder.textViewContent.setText(reviews.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthor;
        private TextView textViewContent;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
