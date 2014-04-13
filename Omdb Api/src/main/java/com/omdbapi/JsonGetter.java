package com.omdbapi;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Tanuj on 29/3/14.
 */
/* A helping class to return a json containing either a search result or info on a specific movie.
    The function getJson takes three arguments method : GET or POST, type : SEARCH or ID, and the String Search_Term containing the search string or the movie id
 */

public class JsonGetter {
    String url_base = "http://api.themoviedb.org";
    String url_genre = "/3/genre/list?";
    String url_id = "http://www.omdbapi.com/?i=";
    String api_key = "api_key=5b94e6f9d10ce92e9e162b000951b505";
    String url_genre_movie = "/3/genre/";
    String url_movie = "/movies?";
    String url_search = "http://www.omdbapi.com/?s=";

    public static final int SEARCH_GENRE = 1;
    public static  final int ID = 2;
    public static final int GENRE = 3;
    public static  final int SEARCH = 4;

    public JsonGetter() {}

    public String getJson(int type ,String Search_Term)
    {   //Http client
        HttpClient httpClient = new DefaultHttpClient();

        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        String response = "";

        try {

            if (type == GENRE )
            {
                HttpGet httpGet = new HttpGet(url_base+url_genre+api_key);
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);

            }
            else if (type == SEARCH_GENRE)
            {
                HttpGet httpGet = new HttpGet(url_base+url_genre_movie+Search_Term+url_movie+api_key);
                Log.d("Request > " , url_base+url_genre_movie+Search_Term+url_movie+api_key);
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }
            else if (type == SEARCH)
            {
                String Encoded_Search_Term = URLEncoder.encode(Search_Term,"UTF-8");
                HttpGet httpGet = new HttpGet(url_search+Encoded_Search_Term);
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }
            else if (type == ID)
            {
                String Encoded_Search_Term = URLEncoder.encode(Search_Term,"UTF-8");
                HttpGet httpGet = new HttpGet(url_id+Encoded_Search_Term);
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }


        }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                } catch (ClientProtocolException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

        return response;
    }
}