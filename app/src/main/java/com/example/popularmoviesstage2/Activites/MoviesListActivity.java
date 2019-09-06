package com.example.popularmoviesstage2.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.popularmoviesstage2.Database.Movie;
import com.example.popularmoviesstage2.Adapters.MoviesListAdapter;
import com.example.popularmoviesstage2.MoviesListViewModel;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.Utils.NetworksUtils;

import java.util.List;

public class MoviesListActivity extends AppCompatActivity {
    private MoviesListViewModel mMoviesListViewModel;

    private Menu menu;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviesListViewModel = ViewModelProviders.of(this).get(MoviesListViewModel.class);
        setContentView(R.layout.list_movies);

        setTitle(mMoviesListViewModel.getFilter());
        swipeLayout = findViewById(R.id.swipe_container);
        RecyclerView mMoviesListRv = mMoviesListRv = findViewById(R.id.list_movies_rv);
        final MoviesListAdapter adapter = new MoviesListAdapter(this);
        adapter.setOnRvItemClickListener(new MoviesListAdapter.onRvItemClickListener() {
            @Override
            public void onItemClicked(Movie movie) {
                //LAUNCH DETAILS ACTIVITY
                Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.POSTER_IMAGE_PATH, movie.getPoster_path());
                intent.putExtra(DetailsActivity.TITLE, movie.getTitle());
                intent.putExtra(DetailsActivity.RELEASE_DATE, movie.getRelease_date());
                intent.putExtra(DetailsActivity.RATING, movie.getVote_average());
                intent.putExtra(DetailsActivity.SYNPOSIS, movie.getOverview());
                intent.putExtra(DetailsActivity.ID, movie.getId());
                intent.putExtra(DetailsActivity.BACKDROP_IMAGE_PATH,movie.getBackdrop_path());
                startActivity(intent);
            }
        });
        mMoviesListRv.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesListRv.setLayoutManager(layoutManager);
        mMoviesListRv.setHasFixedSize(true);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //your method to refresh content
                //we can check filter live data also
                switch (mMoviesListViewModel.getFilter()) {
                    case NetworksUtils.POPULAR_MOVIES_BASE_URL: {
                        mMoviesListViewModel.loadmMoviesMostPopularLiveData();
                        break;
                    }
                    case NetworksUtils.TOP_RATED_MOVIES_BASE_URL: {
                        mMoviesListViewModel.loadmMoviesTopRatedLiveData();
                        break;
                    }
                    case MoviesListViewModel.FAVORITES_LIVE_DATA_FILTER: {
                        //TODO REFRESH FAVORITES
                        swipeLayout.setRefreshing(false);
                        break;
                    }
                }
            }
        });
        mMoviesListViewModel.getMoviesListLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.submitList(movies);
                if (swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        this.menu = menu;

        switch (mMoviesListViewModel.getFilter()) {
            case NetworksUtils.POPULAR_MOVIES_BASE_URL: {
                setTitle(getString(R.string.menu_item_popular_movies));
                menu.findItem(R.id.popular_btn).setChecked(true);
                menu.findItem(R.id.top_rated_btn).setChecked(false);
                menu.findItem(R.id.favorite_btn).setChecked(false);
                break;
            }
            case NetworksUtils.TOP_RATED_MOVIES_BASE_URL: {
                setTitle(getString(R.string.menu_item_top_rated_movies));
                menu.findItem(R.id.popular_btn).setChecked(false);
                menu.findItem(R.id.top_rated_btn).setChecked(true);
                menu.findItem(R.id.favorite_btn).setChecked(false);
                break;
            }
            case MoviesListViewModel.FAVORITES_LIVE_DATA_FILTER: {
                setTitle(getString(R.string.menu_item_favorite));
                menu.findItem(R.id.popular_btn).setChecked(false);
                menu.findItem(R.id.top_rated_btn).setChecked(false);
                menu.findItem(R.id.favorite_btn).setChecked(true);
                break;
            }

        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular_btn: {
                setTitle(getString(R.string.menu_item_popular_movies));
                mMoviesListViewModel.seFilter(NetworksUtils.POPULAR_MOVIES_BASE_URL);
                menu.findItem(R.id.popular_btn).setChecked(true);
                menu.findItem(R.id.top_rated_btn).setChecked(false);
                menu.findItem(R.id.favorite_btn).setChecked(false);
                return true;
            }
            case R.id.top_rated_btn: {
                setTitle(getString(R.string.menu_item_top_rated_movies));
                mMoviesListViewModel.seFilter(NetworksUtils.TOP_RATED_MOVIES_BASE_URL);
                menu.findItem(R.id.popular_btn).setChecked(false);
                menu.findItem(R.id.top_rated_btn).setChecked(true);
                menu.findItem(R.id.favorite_btn).setChecked(false);
                return true;
            }
            case R.id.favorite_btn: {
                setTitle(getString(R.string.menu_item_favorite));
                mMoviesListViewModel.seFilter(MoviesListViewModel.FAVORITES_LIVE_DATA_FILTER);
                menu.findItem(R.id.popular_btn).setChecked(false);
                menu.findItem(R.id.top_rated_btn).setChecked(false);
                menu.findItem(R.id.favorite_btn).setChecked(true);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
