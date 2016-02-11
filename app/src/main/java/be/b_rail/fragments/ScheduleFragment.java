package be.b_rail.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import be.b_rail.Models.Station;
import be.b_rail.R;
import be.b_rail.adapters.StationsAdapter;

/**
 * Created by Jawad on 11-02-16.
 */
public class ScheduleFragment extends BaseFragment  {

    private AutoCompleteTextView    mDepartureStationAutoCompleteTextView;
    private AutoCompleteTextView    mDirectionStationAutoCompleteTextView;

    //private List<String>            responseList;
    private List<Station>           responseStationList;
    private ArrayAdapter<String>    test_adapter;
    private StationsAdapter         stationsAdapter;

    private GetStationsJSONTask		getStationsJSONTask	= null;

    @Override
    public int getTitleResourceId() {
        return R.string.Schedule;
    }
    /**
     * Creates a new instance of a ScheduleFragment.
     */
    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDepartureStationAutoCompleteTextView = (AutoCompleteTextView)getActivity().findViewById(R.id.departure_autoCompleteTextView);
        mDirectionStationAutoCompleteTextView = (AutoCompleteTextView)getActivity().findViewById(R.id.direction_autoCompleteTextView);

        responseStationList = new ArrayList<>();
        mDirectionStationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                Toast.makeText(getActivity(), "Position clicked: " + pos, Toast.LENGTH_SHORT).show();
                   // mDirectionStationAutoCompleteTextView.setText(stationsAdapter.getItem(pos).getName());
                LinearLayout ly = (LinearLayout) arg1;
                TextView tv = (TextView) ly.getChildAt(0);
                mDirectionStationAutoCompleteTextView.setText(tv.getText().toString());
            }
        });

        getStationsJSONTask = new GetStationsJSONTask();
        getStationsJSONTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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

                Log.i("finalJson : ", finalJson);


                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("station");

                Gson gson = new Gson();
                for(int i=0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Station station = gson.fromJson(finalObject.toString(), Station.class);

                    station.setId(finalObject.getString("id"));
                    station.set_Id(Long.parseLong(station.getId().substring(8)));
                    station.setName(finalObject.getString("name"));
                    // adding the final object in the list
                    Log.i("TEST", station.getName() + "ID :" +station.get_Id());
                    responseStationList.add(station);

                   // publishProgress(station);
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
           // Station station = (Station)values[0];
            //stationsAdapter.add(station);
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Void params){

            stationsAdapter = new StationsAdapter(getActivity(),R.layout.item_station,R.id.txtNameStation, responseStationList);
            mDepartureStationAutoCompleteTextView.setAdapter(stationsAdapter);
            mDirectionStationAutoCompleteTextView.setAdapter(stationsAdapter);


        }
    }
}
