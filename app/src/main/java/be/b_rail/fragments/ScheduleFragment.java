package be.b_rail.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import be.b_rail.Models.Station;
import be.b_rail.R;

/**
 * Created by Jawad on 11-02-16.
 */
public class ScheduleFragment extends BaseFragment  {

    private AutoCompleteTextView    mDepartureStationAutoCompleteTextView;
    private AutoCompleteTextView    mDirectionStationAutoCompleteTextView;

    private List<String>            responseList;
    private ArrayAdapter<String>    test_adapter;

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

        responseList = new ArrayList<>();

        // specify an adapters
        test_adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_list_station,R.id.txtNameStation, responseList);


        mDepartureStationAutoCompleteTextView.setAdapter(test_adapter);
        mDirectionStationAutoCompleteTextView.setAdapter(test_adapter);

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
            test_adapter.add(station.getName());
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Void params){



        }
    }
}
