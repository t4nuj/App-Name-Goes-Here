package com.omdbapi;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.HashMap;

public class jsonparser extends ListActivity {

    Intent i;
    String search_term;
    Context c ;
    String url = "http://www.omdbapi.com/?s=";

    private AdapterView.OnItemClickListener  onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            LinearLayout linearLayout =(LinearLayout) view;
            String genre_name = ((TextView) linearLayout.findViewById(R.id.year)).getText().toString();
            Intent intent;
            intent = new Intent(c , MainActivity2.class );
            intent.putExtra("Genre_Selected", genre_name);
            startActivity(intent);
        }
    };

    //Tags For the keys of OMDB API JSON object received
    protected static final String TAG_GENRE = "genres";
    protected static final String TAG_TITLE = "name";
    protected static final String TAG_ID = "id";
    protected static final String TAG_YEAR =  "Year";
    protected static final String TAG_RESPONSE = "Response";

    //Array to populate the list
    ArrayList<HashMap<String,String> > MovieList;

    //List View To display the search result
    ListView listView;

    //A progress dialog to indicate the user that the search query is being processed
    ProgressDialog p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonparser);
        c = this;

        i = getIntent();
        search_term = i.getStringExtra("Search_Term");

        listView = getListView();

        MovieList = new ArrayList<HashMap<String, String>>();
        GetMovies getMovies =  new GetMovies();
        getMovies.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.jsonparser, menu);
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
    private class GetMovies extends AsyncTask<Void, Void, Void>{

       @Override
        protected void onPreExecute()
       {
           super.onPreExecute();
           p = new ProgressDialog(jsonparser.this);
           p.setTitle(getString(R.string.please_wait));
           p.show();
       }

        @Override
        protected Void  doInBackground(Void... params)
        {
            JsonGetter jsonGetter = new JsonGetter();
            String jsonResponse = jsonGetter.getJson(JsonGetter.GENRE,search_term);
            Log.d("Response : ",">" + jsonResponse);
            JSONObject Search_Results = new JSONObject();
            if (jsonResponse!=null)
            {
                try {
                    Search_Results = new JSONObject(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (!((Search_Results.has(TAG_RESPONSE)) && (Search_Results.getString(TAG_RESPONSE).equals("false"))))
                    {   try {
                                JSONArray Genre_List = Search_Results.getJSONArray(TAG_GENRE);
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

                                    MovieList.add(temporary_list);


                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                    }
                } catch (JSONException e) {
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

            ListAdapter listAdapter = new SimpleAdapter(jsonparser.this,MovieList,R.layout.list_item,new String[] {TAG_TITLE,TAG_ID},new int[] {R.id.title,R.id.year});
            setListAdapter(listAdapter);
            listView.setClickable(true);

            listView.setOnItemClickListener(onItemClickListener);
        }
    }


}
