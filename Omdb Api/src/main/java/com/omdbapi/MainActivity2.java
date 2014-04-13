package com.omdbapi;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends ListActivity{

    TextView textView;
    ProgressDialog p;
    ListView listView;

    String SELECTED_GENRE;
    String TAG_RESULTS = "results";
    String TAG_ID = "id";
    String TAG_TITLE = "title";

    ArrayList<HashMap<String,String> > GenreList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        SELECTED_GENRE = intent.getStringExtra("Genre_Selected");

        listView = getListView();

        GenreList = new ArrayList<HashMap<String, String>>();

        GetMoviesOfGenre getMoviesOfGenre = new GetMoviesOfGenre();
        getMoviesOfGenre.execute();

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

    private class GetMoviesOfGenre extends AsyncTask<Void, Void, Void> {

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
            String jsonResponse = jsonGetter.getJson(JsonGetter.SEARCH_GENRE,SELECTED_GENRE);
            Log.d("Response : ", ">" + jsonResponse);
            JSONObject Search_Results = new JSONObject();
            if (jsonResponse!=null)
            {
                try {
                    Search_Results = new JSONObject(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                   JSONArray Genre_List = Search_Results.getJSONArray(TAG_RESULTS);
                   for (int i = 0; i<Genre_List.length(); i++)
                   {
                       JSONObject genre = (JSONObject) Genre_List.get(i);
                       String id = genre.getString(TAG_ID);
                       String title = genre.getString(TAG_TITLE);
                       //String year = movie.getString(TAG_YEAR);

                       //Temporary list to store values from current JSON object
                       HashMap<String,String> temporary_list = new HashMap<String, String>();

                       temporary_list.put(TAG_ID,id);
                       temporary_list.put(TAG_TITLE,title);
                       //temporary_list.put(TAG_YEAR,year);

                       GenreList.add(temporary_list);


                   }
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

            ListAdapter listAdapter = new SimpleAdapter(MainActivity2.this,GenreList,R.layout.list_item,new String[] {TAG_TITLE,TAG_ID},new int[] {R.id.title,R.id.year});
            setListAdapter(listAdapter);

        }
    }



}
