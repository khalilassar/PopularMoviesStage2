package com.example.popularmoviesstage2.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesstage2.Database.Movie;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.Review;
import com.example.popularmoviesstage2.Utils.NetworksUtils;
import com.squareup.picasso.Picasso;


public class ReviewsAdapter extends ListAdapter<Review, ReviewsAdapter.ReviewViewHolder> {

    private static final DiffUtil.ItemCallback<Review> DIFF_CALLBACK = new DiffUtil.ItemCallback<Review>() {
        @Override
        public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.getmAuthor().equals(newItem.getmAuthor());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.getmContent().equals(newItem.getmContent());
        }
    };

    public ReviewsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getReviewAt(position);
        holder.mAuthorTv.setText(review.getmAuthor());
        holder.mContentTv.setText(review.getmContent());


    }

    private Review getReviewAt(int position) {
        return getItem(position);

    }

    protected class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthorTv;
        private TextView mContentTv;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorTv = itemView.findViewById(R.id.authour_tv);
            mContentTv = itemView.findViewById(R.id.content_tv);

        }
    }

}
