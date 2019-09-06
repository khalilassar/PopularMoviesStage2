package com.example.popularmoviesstage2.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesstage2.Database.Movie;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.Utils.NetworksUtils;
import com.squareup.picasso.Picasso;

public class MoviesListAdapter extends ListAdapter<Movie, MoviesListAdapter.MovieViewHolder> {
    private Context context;
    private onRvItemClickListener mListener;
    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getPoster_path().equals(newItem.getPoster_path());
        }
    };

    public MoviesListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(NetworksUtils.IMAGE_BASE_URL + getMovieAt(position).getBackdrop_path())).fit().into(holder.mImgItem);

    }

    private Movie getMovieAt(int position) {
        return getItem(position);


    }


    protected class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgItem;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mImgItem = itemView.findViewById(R.id.movie_item_lmg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(getMovieAt(getAdapterPosition()));
                }
            });
        }
    }

    public interface onRvItemClickListener {
        void onItemClicked(Movie movie);
    }

    public void setOnRvItemClickListener(onRvItemClickListener Listener) {
        mListener = Listener;
    }

}
