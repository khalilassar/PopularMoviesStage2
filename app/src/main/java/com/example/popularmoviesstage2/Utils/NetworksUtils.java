package com.example.popularmoviesstage2.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class NetworksUtils {
    private static final String MOVIES_DB_API_KEY = "7ee31af5f1040fd498104f1140e75b39";
    public static final String POPULAR_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    public static final String TOP_RATED_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated";
    public static final String ABSOLUTE_BATH="https://api.themoviedb.org/3/movie/";
    public static final String ABSOLUTE_BATH_VIDEOS_CONST="/videos";
    public static final String ABSOLUTE_BATH_REVIEWS_CONST="/reviews";



    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String API_PARAMETER = "api_key";

    public static final String RESPONSE_OK = "ok";
    public static final String RESPONSE_BAD = "error";
    public static final String RESPONSE_SERVER_ERROR_HTTP_NOT_FOUND = "error,HTTP_NOT_FOUND";
    public static final String RESPONSE_SERVER_ERROR_HTTP_UNAUTHORIZED = "error,HTTP_UNAUTHORIZED";
    public static final String RESPONSE_BAD_UNKNOWN_RESPONSE_ERROR = "error,UNKNOWN_RESPONSE_ERROR ";
    public static final String RESPONSE_HTTP_CONNECTION_EXCEPTION = "no internet";

    public static URL getURL(String baseURL) {
        Uri uri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(API_PARAMETER, MOVIES_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.d("ddd", e.toString());
        }
        return url;
    }

    public static String getResponse(URL url)  {
        HttpURLConnection urlConnection = null;
        String response = RESPONSE_BAD;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = readStream(urlConnection.getInputStream());
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                response = RESPONSE_SERVER_ERROR_HTTP_NOT_FOUND;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                response = RESPONSE_SERVER_ERROR_HTTP_UNAUTHORIZED;
            } else {
                response = RESPONSE_BAD_UNKNOWN_RESPONSE_ERROR;
            }
        }
        catch(IOException e){
            Log.d("ddd"," getResponse IOException "+ e);
            response= RESPONSE_HTTP_CONNECTION_EXCEPTION;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            return response;
        }

    }

    private static String readStream(InputStream in)  {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while (true) {
                try {
                    if ((line = reader.readLine()) == null) break;
                } catch (IOException e) {
                    Log.d("ddd", e.toString());
                    return RESPONSE_BAD;
                }
                response.append(line);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d("ddd", e.toString());
                    return RESPONSE_BAD;
                }
            }
        }
        return response.toString();
    }
}
