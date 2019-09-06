package com.example.popularmoviesstage2.Activites;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmoviesstage2.Adapters.MoviesListAdapter;
import com.example.popularmoviesstage2.Adapters.ReviewsAdapter;
import com.example.popularmoviesstage2.Adapters.TrailersAdapter;
import com.example.popularmoviesstage2.Database.Movie;
import com.example.popularmoviesstage2.Database.MovieDao;
import com.example.popularmoviesstage2.Database.MyRoomDatabase;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.Review;
import com.example.popularmoviesstage2.Utils.MoviesJSONParser;
import com.example.popularmoviesstage2.Utils.NetworksUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private ImageView posterImg;
    private TextView titleTv;
    private TextView releaseDate;
    private RatingBar ratingRb;
    private TextView synposisTv;
    private Button favoriteStarBtn;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String POSTER_IMAGE_PATH = "posterImg";
    public static final String BACKDROP_IMAGE_PATH = "backdropImg";
    public static final String TITLE = "titleTv";
    public static final String RELEASE_DATE = "releaseDate";
    public static final String RATING = "ratingRb";
    public static final String SYNPOSIS = "synposisTv";
    public static final String ID = "ID";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String favoriteStarBtnWhite = "White";
    public static final String favoriteStarBtnYellow = "Yellow";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Movie mMovie = null;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private IsInFavoritesAsyncTask isInFavoritestask;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private SwipeRefreshLayout trailerSwipeLayout;
    private SwipeRefreshLayout reviewSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        extractIntentIntomMovies();
        favoriteStarBtn = findViewById(R.id.button_star);
        configurefavoriteStarBtn(mMovie.getId());
        posterImg = findViewById(R.id.image_iv);
        titleTv = findViewById(R.id.title_tv);
        releaseDate = findViewById(R.id.release_date_tv);
        ratingRb = findViewById(R.id.ratings_rb);
        synposisTv = findViewById(R.id.synopsis_tv);
        trailerSwipeLayout = findViewById(R.id.trailer_swipe_container);
        reviewSwipeLayout=findViewById(R.id.review_swipe_container);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        RecyclerView TrailersRv = findViewById(R.id.trailersRv);
        final TrailersAdapter adapter = new TrailersAdapter(this);
        adapter.setOnRvItemClickListener(new TrailersAdapter.onRvItemClickListener() {
            @Override
            public void onItemClicked(String key) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(TrailersAdapter.YOUTUBE_IMG_URL_START + key));
                try {
                    getBaseContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    getBaseContext().startActivity(webIntent);
                }
            }
        });
        TrailersRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        TrailersRv.setLayoutManager(layoutManager);
        TrailersRv.setHasFixedSize(true);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        RecyclerView ReviewsRv = findViewById(R.id.reviewsRv);
        final ReviewsAdapter adapterReview = new ReviewsAdapter();
        ReviewsRv.setAdapter(adapterReview);
        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ReviewsRv.setLayoutManager(layoutManagerReview );
        ReviewsRv.setHasFixedSize(true);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        updateViewsFrommMovies();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        favoriteStarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getBtnStarColor().equals(favoriteStarBtnWhite)) {
                    //favorite
                    setBtnStarColorYellow();
                    Toast.makeText(getBaseContext(), "favorite", Toast.LENGTH_SHORT).show();
                    final MyRoomDatabase database = MyRoomDatabase.getInstance(getApplicationContext());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            database.movieDao().insert(mMovie);
                        }
                    });
                } else if (getBtnStarColor().equals(favoriteStarBtnYellow)) {
                    //unfavorite
                    setBtnStarColorWhite();
                    Toast.makeText(getBaseContext(), "unfavorite", Toast.LENGTH_SHORT).show();
                    final MyRoomDatabase database = MyRoomDatabase.getInstance(getApplicationContext());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            database.movieDao().delete(mMovie);
                        }
                    });
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Instantiate the RequestQueue.
        trailerSwipeLayout.setRefreshing(true);
        reviewSwipeLayout.setRefreshing(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = NetworksUtils.getURL(NetworksUtils.ABSOLUTE_BATH + mMovie.getId() + NetworksUtils.ABSOLUTE_BATH_VIDEOS_CONST).toString();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> trailers = MoviesJSONParser.parseMoviesTrailers(response);
                        adapter.submitList(trailers);
                        trailerSwipeLayout.setRefreshing(false);
                        trailerSwipeLayout.setEnabled(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "no internet", Toast.LENGTH_SHORT).show();
                trailerSwipeLayout.setRefreshing(false);
                trailerSwipeLayout.setEnabled(false);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        RequestQueue queueReview = Volley.newRequestQueue(this);
        String urlReview = NetworksUtils.getURL(NetworksUtils.ABSOLUTE_BATH + mMovie.getId() + NetworksUtils.ABSOLUTE_BATH_REVIEWS_CONST).toString();

// Request a string response from the provided URL.
        StringRequest stringRequestReview = new StringRequest(Request.Method.GET, urlReview,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Review> Reviews = MoviesJSONParser.parseMoviesReviews(response);
                        for (Review review:Reviews) {
                            Log.d("dddddddddd",review.getmAuthor()+"     "+review.getmContent());

                        }
                        adapterReview.submitList(Reviews);
                        reviewSwipeLayout.setRefreshing(false);
                        reviewSwipeLayout.setEnabled(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "no internet", Toast.LENGTH_SHORT).show();
                reviewSwipeLayout.setRefreshing(false);
                reviewSwipeLayout.setEnabled(false);
            }
        });

// Add the request to the RequestQueue.
        queueReview.add(stringRequestReview);















    }

    @Override
    protected void onDestroy() {
        isInFavoritestask.setListener(null);
        super.onDestroy();
    }

    private void setBtnStarColorWhite() {
        favoriteStarBtn.setTag(favoriteStarBtnWhite);
        favoriteStarBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        favoriteStarBtn.setVisibility(View.VISIBLE);
    }

    private void setBtnStarColorYellow() {
        favoriteStarBtn.setTag(favoriteStarBtnYellow);
        favoriteStarBtn.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
        favoriteStarBtn.setVisibility(View.VISIBLE);
    }

    private String getBtnStarColor() {
        return favoriteStarBtn.getTag().toString();
    }

    private IsInFavoritesListener createIsInFavoritesListener() {
        IsInFavoritesListener listener = new IsInFavoritesListener() {
            @Override
            public void howManyTimesINDB(int timesInDB) {
                if (timesInDB == 0) {
                    setBtnStarColorWhite();
                } else if (timesInDB == 1) {
                    setBtnStarColorYellow();
                } else {
                    Toast.makeText(getBaseContext(), "error" + timesInDB, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        return listener;
    }

    private void configurefavoriteStarBtn(int id) {
        isInFavoritestask = new IsInFavoritesAsyncTask(MyRoomDatabase.getInstance(getApplicationContext()).movieDao());
        isInFavoritestask.setListener(createIsInFavoritesListener());
        isInFavoritestask.execute(id);
    }

    private void extractIntentIntomMovies() {
        Intent intent = getIntent();
        if (intent != null) {
            mMovie = new Movie();
            mMovie.setId(intent.getIntExtra(ID, -1));
            Log.d("ddd", String.valueOf(intent.getIntExtra(ID, -1)));
            mMovie.setTitle(intent.getStringExtra(TITLE));
            mMovie.setRelease_date(intent.getStringExtra(RELEASE_DATE));
            mMovie.setVote_average(intent.getDoubleExtra(RATING, 0));
            mMovie.setOverview(intent.getStringExtra(SYNPOSIS));
            mMovie.setBackdrop_path(intent.getStringExtra(BACKDROP_IMAGE_PATH));
            mMovie.setPoster_path(intent.getStringExtra(POSTER_IMAGE_PATH));
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateViewsFrommMovies() {
        setTitle(mMovie.getTitle());
        titleTv.setText(mMovie.getTitle());
        releaseDate.setText(mMovie.getRelease_date());
        ratingRb.setRating((float) mMovie.getVote_average());
        synposisTv.setText(mMovie.getOverview());
        Picasso.get().load(Uri.parse(NetworksUtils.IMAGE_BASE_URL + mMovie.getPoster_path())).fit().into(posterImg);
    }

    interface IsInFavoritesListener {
        void howManyTimesINDB(int timesInDB);
    }

    public static class IsInFavoritesAsyncTask extends AsyncTask<Integer, Void, Integer> {
        private IsInFavoritesListener listener;
        private MovieDao movieDao;

        public IsInFavoritesAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return howManyTimesINDB(integers[0]);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            if (listener != null) {
                listener.howManyTimesINDB(integer);
            }
        }

        private int howManyTimesINDB(int id) {
            return movieDao.getFavoriteMovieCount(id);
        }

        public void setListener(IsInFavoritesListener listener) {
            this.listener = listener;
        }
    }

}
