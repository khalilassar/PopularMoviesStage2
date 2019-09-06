package com.example.popularmoviesstage2;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.popularmoviesstage2.Database.Movie;
import com.example.popularmoviesstage2.Database.MyRoomDatabase;
import com.example.popularmoviesstage2.Utils.MoviesJSONParser;
import com.example.popularmoviesstage2.Utils.NetworksUtils;

import java.net.URL;
import java.util.List;

public class MoviesListViewModel extends AndroidViewModel {
    private Application application;
    private MutableLiveData<String> mMoviesFilterLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> mMoviesMostPopularLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> mMoviesTopRatedLiveData = new MutableLiveData<>();
    private MediatorLiveData<List<Movie>> mMoviesLiveData = new MediatorLiveData<>();
    private LiveData<List<Movie>> mMoviesFavorietsLiveData;
    public static final String FAVORITES_LIVE_DATA_FILTER="F";
    public MoviesListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        mMoviesFavorietsLiveData= MyRoomDatabase.getInstance(application).movieDao().getAllFavoriteLiveData();
        /////////////////////////////////////////////////////////////////////////////
        mMoviesLiveData.addSource(mMoviesFilterLiveData, new Observer<String>() {
            @Override
            public void onChanged(String filter) {
                //SET mMoviesLiveData BASED ON FILTER
                //apply filter
                Log.d("ddd", " onChangedmMoviesFilterLiveData");
                switch (filter) {
                    case NetworksUtils.POPULAR_MOVIES_BASE_URL: {

                        Log.d("ddd", "before mMoviesLiveData.setValue(mMoviesMostPopularLiveData.getValue());");
                        mMoviesLiveData.setValue(mMoviesMostPopularLiveData.getValue());
                        break;

                    }
                    case NetworksUtils.TOP_RATED_MOVIES_BASE_URL: {
                        Log.d("ddd", "before mMoviesLiveData.setValue(mMoviesTopRatedLiveData.getValue());");
                        mMoviesLiveData.setValue(mMoviesTopRatedLiveData.getValue());
                        break;

                    }
                    case FAVORITES_LIVE_DATA_FILTER: {
                        Log.d("ddd", "before mMoviesLiveData.setValue(mMoviesTopRatedLiveData.getValue());");
                        mMoviesLiveData.setValue(mMoviesFavorietsLiveData.getValue());
                        break;

                    }
                }
            }
        });
        mMoviesLiveData.addSource(mMoviesMostPopularLiveData, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //explicitly notifies  mMoviesLiveData
                Log.d("ddd", " onChanged  mMoviesMostPopularLiveData ");
                mMoviesFilterLiveData.setValue(mMoviesFilterLiveData.getValue());
            }
        });
        mMoviesLiveData.addSource(mMoviesTopRatedLiveData, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //explicitly notifies  mMoviesLiveData
                Log.d("ddd", " onChanged  mMoviesTopRatedLiveData");
                mMoviesFilterLiveData.setValue(mMoviesFilterLiveData.getValue());
            }
        });
        mMoviesLiveData.addSource(mMoviesFavorietsLiveData, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //explicitly notifies  mMoviesLiveData
                Log.d("ddd", " onChanged  mMoviesFavorietsLiveData");
                mMoviesFilterLiveData.setValue(mMoviesFilterLiveData.getValue());
            }
        });

        //set default filter
        mMoviesFilterLiveData.setValue(FAVORITES_LIVE_DATA_FILTER);
        //Load data For the First Time
        loadmMoviesMostPopularLiveData();
        //Load data For the First Time
        loadmMoviesTopRatedLiveData();
    }

    public void loadmMoviesMostPopularLiveData() {
        new getMoviesListFromNetworkAsyncTask().execute(NetworksUtils.POPULAR_MOVIES_BASE_URL);
    }

    public void loadmMoviesTopRatedLiveData() {
        new getMoviesListFromNetworkAsyncTask().execute(NetworksUtils.TOP_RATED_MOVIES_BASE_URL);
    }


    public LiveData<List<Movie>> getMoviesListLiveData() {
        return mMoviesLiveData;
    }

    public void seFilter(String filter) {
        mMoviesFilterLiveData.setValue(filter);
    }

    public String getFilter() {
        return mMoviesFilterLiveData.getValue();
    }

    private class getMoviesListFromNetworkAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... URLs) {
            String response;
            String responseState = NetworksUtils.RESPONSE_BAD;
            final String popularMoviesList = "popular movies";
            final String topRatedMovieslist = "toprated movies";
            URL url;
            switch (URLs[0]) {
                case NetworksUtils.POPULAR_MOVIES_BASE_URL: {

                    Log.d("ddd", "before getURL case NetworksUtils.POPULAR_MOVIES_BASE_URL:");
                    url = NetworksUtils.getURL(NetworksUtils.POPULAR_MOVIES_BASE_URL);
                    Log.d("ddd", "before getResponseFromHttpUrl case NetworksUtils.POPULAR_MOVIES_BASE_URL:");
                    response = NetworksUtils.getResponse(url);
                    switch (response) {
                        case NetworksUtils.RESPONSE_HTTP_CONNECTION_EXCEPTION: {
                            responseState = NetworksUtils.RESPONSE_HTTP_CONNECTION_EXCEPTION +" can't update " +popularMoviesList;
                            break;
                        }
                        case NetworksUtils.RESPONSE_BAD: {
                            responseState = NetworksUtils.RESPONSE_BAD +" "+ popularMoviesList;
                            break;
                        }
                        case NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_NOT_FOUND: {
                            responseState = NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_NOT_FOUND +" "+ popularMoviesList;
                            break;
                        }
                        case NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_UNAUTHORIZED: {
                            responseState = NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_UNAUTHORIZED +" "+popularMoviesList;
                            break;
                        }
                        case NetworksUtils.RESPONSE_BAD_UNKNOWN_RESPONSE_ERROR: {
                            responseState = NetworksUtils.RESPONSE_BAD_UNKNOWN_RESPONSE_ERROR +" "+ popularMoviesList;
                            break;
                        }
                        default: {
                            Log.d("ddd", "before MoviesJSONParser().parseMoviesJSON(response); case NetworksUtils.POPULAR_MOVIES_BASE_URL:");
                            List<Movie> list = new MoviesJSONParser().parseMoviesJSON(response);
                            Log.d("ddd", "before postValue(list); case NetworksUtils.POPULAR_MOVIES_BASE_URL:");
                            mMoviesMostPopularLiveData.postValue(list);
                            Log.d("ddd", "after postValue(list); case NetworksUtils.POPULAR_MOVIES_BASE_URL:");
                            responseState =popularMoviesList+" updated ";
                            Log.d("ddd", "after esponseState = NetworksUtils.RESPONSE_OK; case NetworksUtils.POPULAR_MOVIES_BASE_URL:");
                            break;
                        }
                    }
                    break;
                }
                case NetworksUtils.TOP_RATED_MOVIES_BASE_URL: {
                    Log.d("ddd", "before getURL case NetworksUtils.TOP_RATED_MOVIES_BASE_URL:");
                    url = NetworksUtils.getURL(NetworksUtils.TOP_RATED_MOVIES_BASE_URL);
                    Log.d("ddd", "before getResponseFromHttpUrl case NetworksUtils.TOP_RATED_MOVIES_BASE_URL:");
                    response = NetworksUtils.getResponse(url);
                    switch (response) {
                        case NetworksUtils.RESPONSE_HTTP_CONNECTION_EXCEPTION: {
                            responseState = NetworksUtils.RESPONSE_HTTP_CONNECTION_EXCEPTION +" can't update " +topRatedMovieslist;
                            break;
                        }
                        case NetworksUtils.RESPONSE_BAD: {
                            responseState = NetworksUtils.RESPONSE_BAD +" "+ topRatedMovieslist;
                            break;
                        }
                        case NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_NOT_FOUND: {
                            responseState = NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_NOT_FOUND +" "+ topRatedMovieslist;
                            break;
                        }
                        case NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_UNAUTHORIZED: {
                            responseState = NetworksUtils.RESPONSE_SERVER_ERROR_HTTP_UNAUTHORIZED +" "+ topRatedMovieslist;
                            break;
                        }
                        case NetworksUtils.RESPONSE_BAD_UNKNOWN_RESPONSE_ERROR: {
                            responseState = NetworksUtils.RESPONSE_BAD_UNKNOWN_RESPONSE_ERROR + " "+topRatedMovieslist;
                            break;
                        }
                        default: {
                            Log.d("ddd", "before MoviesJSONParser().parseMoviesJSON(response); case NetworksUtils.TOP_RATED_MOVIES_BASE_URL:");
                            List<Movie> list = new MoviesJSONParser().parseMoviesJSON(response);
                            Log.d("ddd", "before postValue(list); case NetworksUtils.TOP_RATED_MOVIES_BASE_URL:");
                            mMoviesTopRatedLiveData.postValue(list);
                            Log.d("ddd", "after postValue(list); case NetworksUtils.TOP_RATED_MOVIES_BASE_URL:");
                            responseState =topRatedMovieslist+" updated ";
                            Log.d("ddd", "after esponseState = NetworksUtils.RESPONSE_OK; case NetworksUtils.TOP_RATED_MOVIES_BASE_URL:");
                            break;
                        }
                    }
                    break;
                }
                default:
                    responseState = NetworksUtils.RESPONSE_BAD;
                    return responseState;
            }
            return responseState;
        }

        @Override
        protected void onPostExecute(String responseState) {
            Toast.makeText(application, responseState, Toast.LENGTH_SHORT).show();
        }
    }
}
