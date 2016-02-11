package be.b_rail.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import be.b_rail.Models.Station;
import be.b_rail.R;
import be.b_rail.adapters.AdapterRecyclerStations;

/**
 * Created by Jawad on 09-02-16.
 */
public class StationsFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private Snackbar                            snackBar;
    // The SearchView for doing filtering.
    private SearchView                          mSearchView;
    // If non-null, this is the current filter the user has provided.
    private String                              mCurFilter;

    private boolean 							list_visible 		= true;

    private AdapterRecyclerStations             mAdapter;
    private RecyclerView.LayoutManager 		 	mLayoutManager;
    private RecyclerView 						mStationsRecyclerView;


    private GetStationsJSONTask					getStationsJSONTask	= null;



    @Override
    public int getTitleResourceId() {
        return R.string.Stations;
    }

    /**
     * Creates a new instance of a StationsFragment.
     */
    public static StationsFragment newInstance() {
        return new StationsFragment();
    }
    /***************************************************************
     * Menu*********************************************************
     ***************************************************************/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_stations, menu);
        // Place an action bar item for searching.

        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
       /* boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);*/
        MenuItem menuItem = menu.findItem(R.id.action_type_view);
        if (list_visible) {
            menuItem.setTitle(R.string.type_grid);
        } else {
            menuItem.setTitle(R.string.type_list);
        }
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {

            case R.id.action_search: return true;
            case R.id.action_type_view: return true;

            default:return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_stations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStationsRecyclerView	= (RecyclerView)getActivity().findViewById(R.id.stations_recyclerview);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mStationsRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapters

        mAdapter = new AdapterRecyclerStations(getActivity());

        mStationsRecyclerView.setAdapter(mAdapter);


        getStationsJSONTask = new GetStationsJSONTask();
        getStationsJSONTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
    /*****************************************************************
     * Search*********************************************************
     ***************************************************************/
    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        clearSearch();
        return true;
    }
    private void clearSearch(){
        try {
            mAdapter.getFilter().filter("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
        // Don't do anything if the filter hasn't actually changed.
        // Prevents restarting the loader when restoring state.
        if (mCurFilter == null && newFilter == null) {
            return true;
        }
        if (mCurFilter != null && mCurFilter.equals(newFilter)) {
            return true;
        }
        mCurFilter = newFilter;
        //getLoaderManager().restartLoader(0, null, this);

        try {
            mAdapter.getFilter().filter(newText.toString());
            //txtNbContacts.setText(Html.fromHtml("<font color=\"blue\"><b>"+mAdapter.getCount_filter() + "</b></font> " + getString(R.string.contactss)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
       try{
            if(getStationsJSONTask != null)getStationsJSONTask.cancel(true);
        }catch(IllegalStateException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    /*************************************************************************************************************************/
    private class GetStationsJSONTask extends AsyncTask<Void, Object, Void> {

        private static final String TAG = "GetStationsTask";
        //private static final String SERVER_URL = "https://irail.be/stations/NMBS";
        private static final String SERVER_URL = "stationsfr.json";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
               // URL url = new URL(SERVER_URL);
               // connection = (HttpURLConnection) url.openConnection();


                reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(SERVER_URL)));



             /* connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setRequestProperty("Content-type", "application/json");
                connection.connect();*/

                //InputStream stream = connection.getInputStream();
                //reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                Log.i("finalJson : ",finalJson);


                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("station");

                List<Station> stationList = new ArrayList<>();


                Gson gson = new Gson();
                for(int i=0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Station station = gson.fromJson(finalObject.toString(), Station.class);

                    station.setIdStation(finalObject.getString("id"));
                    station.setName(finalObject.getString("name"));
                    // adding the final object in the list
                    Log.i("TEST", station.getName());
                    stationList.add(station);

                    publishProgress(station);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }
        @Override
        protected void onProgressUpdate(Object... values) {
            Station station = (Station)values[0];
            mAdapter.add(station);
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Void params){



        }
    }

}
