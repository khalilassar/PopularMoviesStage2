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


public class TrailersAdapter extends ListAdapter<String,TrailersAdapter.TrailerViewHolder> {
    public final static String YOUTUBE_IMG_URL_START="http://img.youtube.com/vi/";
    public final static String YOUTUBE_IMG_URL_END="/mqdefault.jpg";

    private Context context;
    private TrailersAdapter.onRvItemClickListener mListener;
    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK=new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TrailersAdapter(Context context) {
        super(DIFF_CALLBACK);

    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(YOUTUBE_IMG_URL_START+getKeyAt(position)+YOUTUBE_IMG_URL_END)).fit().into(holder.mImgItem);
    }
    private String getKeyAt(int position) {
        return getItem(position);

    }

    protected class TrailerViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgItem;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mImgItem = itemView.findViewById(R.id.trailer_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mListener.onItemClicked(getKeyAt(getAdapterPosition()));
                }
            });
        }
    }
    public interface onRvItemClickListener {
        void onItemClicked(String key);
    }

    public void setOnRvItemClickListener(TrailersAdapter.onRvItemClickListener Listener) {
        mListener = Listener;
    }
}
