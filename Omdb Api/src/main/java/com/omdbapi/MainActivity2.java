package com.omdbapi;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends Activity{

    TextView title_of_movie;
    TextView plot_of_movie;
    ImageView movie_poster;
    ProgressDialog p;
    Uri uri;
    //tags
    String imdbID;
    String TAG_POSTER = "Poster";
    String TAG_YEAR = "Year";
    String TAG_TITLE = "Title";
    String TAG_PLOT = "Plot";


    //Actual data
    String Movie_title;
    String Movie_plot;
    String Movie_year;
    String Poster_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        imdbID = intent.getStringExtra("ImdbID");

        title_of_movie = (TextView) findViewById(R.id.textView_title);
        plot_of_movie = (TextView) findViewById(R.id.textView_Plot);
        movie_poster = (ImageView) findViewById(R.id.imageView_Poster);




        GetMoviesById getMoviesById = new GetMoviesById();
        getMoviesById.execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetMoviesById extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            p = new ProgressDialog(MainActivity2.this);
            p.setTitle(getString(R.string.please_wait));
            p.show();
        }

        @Override
        protected Void  doInBackground(Void... params)
        {
            JsonGetter jsonGetter = new JsonGetter();
            String jsonResponse = jsonGetter.getJson(JsonGetter.ID,imdbID);
            Log.d("Response : ", ">" + jsonResponse);
            JSONObject Request_Response = new JSONObject();
            if (jsonResponse!=null)
            {
                try {
                    Request_Response = new JSONObject(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                  Movie_title = Request_Response.getString(TAG_TITLE);
                  Movie_plot = Request_Response.getString(TAG_PLOT);
                  Movie_year = Request_Response.getString(TAG_YEAR);
                  Poster_url = Request_Response.getString(TAG_POSTER);
               }
               catch (JSONException e)
               {
                   e.printStackTrace();
               }

            }

            return null;
        }


        @Override
        public void onPostExecute(Void v)
        {
            super.onPreExecute();
            /*if(p.isShowing())
                {*/
            p.dismiss();
               /* }*/
            title_of_movie.setText(Movie_title);
            plot_of_movie.setText(Movie_plot);

            uri = Uri.parse(Poster_url);
            movie_poster.setImageURI(uri);


        }
    }



}
