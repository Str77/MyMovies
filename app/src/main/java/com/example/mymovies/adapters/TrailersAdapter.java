package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Trailer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {

    private ArrayList<Trailer> trailers;

    private OnClickImagePlayIcon onClickImagePlayIcon;

    public interface OnClickImagePlayIcon {
        void onClickImage(URL url);
    }
    public void setOnClickImagePlayIcon(OnClickImagePlayIcon onClickImagePlayIcon) {
        this.onClickImagePlayIcon = onClickImagePlayIcon;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        holder.textViewNameOfTrailer.setText(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewPlayerIcon;
        private TextView textViewNameOfTrailer;

        public TrailerHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPlayerIcon = itemView.findViewById(R.id.imageViewPlayerIcon);
            textViewNameOfTrailer = itemView.findViewById(R.id.textViewNameOfTrailer);
            imageViewPlayerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickImagePlayIcon != null) {
                        URL url = null;
                        try {
                            url = new URL((trailers.get(getAdapterPosition()).getYoutubeURLTrailer()));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        onClickImagePlayIcon.onClickImage(url);
                    }
                }
            });
        }
    }
}
