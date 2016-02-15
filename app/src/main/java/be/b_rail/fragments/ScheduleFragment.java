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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import be.b_rail.Models.Connection;
import be.b_rail.Models.Station;
import be.b_rail.R;
import be.b_rail.adapters.StationsAdapter;

/**
 * Created by Jawad on 11-02-16.
 */
public class ScheduleFragment extends BaseFragment  {

    private AutoCompleteTextView    mDepartureStationAutoCompleteTextView;
    private AutoCompleteTextView    mDirectionStationAutoCompleteTextView;
    private TextView                responseTextView;
    private Button                  requestButton;

    private List<Station>           responseStationList;
    private StationsAdapter         stationsAdapter;

    private GetStationsJSONTask		getStationsJSONTask	= null;
    //private GetConnectionsJSONTask  getConnectionsJSONTask = null;

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
        responseTextView = (TextView)getActivity().findViewById(R.id.responseTextView);
        requestButton = (Button)getActivity().findViewById(R.id.requestButton);

        responseStationList = new ArrayList<>();

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getConnectionsJSONTask = new GetConnectionsJSONTask();
//                getConnectionsJSONTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                ConnectionFragment connectionFragment = new ConnectionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Departure",mDepartureStationAutoCompleteTextView.getText().toString());
                bundle.putString("Arrival",mDirectionStationAutoCompleteTextView.getText().toString());
                connectionFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container,connectionFragment,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        getStationsJSONTask = new GetStationsJSONTask();
        getStationsJSONTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
    @Override
    public void onDestroy() {
        try{
            if(getStationsJSONTask != null)getStationsJSONTask.cancel(true);
           // if(getConnectionsJSONTask != null)getConnectionsJSONTask.cancel(true);
        }catch(IllegalStateException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private class GetStationsJSONTask extends AsyncTask<Void, Object, Void> {

        private static final String TAG = "GetStationsTask";
        private static final String SERVER_URL = "stationsfr.json";

        @Override
        protected Void doInBackground(Void... params) {
            BufferedReader reader = null;

            try {

                reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(SERVER_URL)));

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
                    station.setStandardname(finalObject.getString("standardname"));
                    // adding the final object in the list
                    responseStationList.add(station);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
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
