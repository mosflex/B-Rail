package be.b_rail.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import be.b_rail.ConnectionActivity;
import be.b_rail.Models.Station;
import be.b_rail.R;
import be.b_rail.adapters.StationsAdapter;

/**
 * Created by Jawad on 11-02-16.
 */
public class ScheduleFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AutoCompleteTextView    mDepartureStationAutoCompleteTextView;
    private AutoCompleteTextView    mDirectionStationAutoCompleteTextView;
    private FloatingActionButton    requestButton;

    private List<Station>           responseStationList;
    private StationsAdapter         stationsAdapter;

    private GetStationsJSONTask		getStationsJSONTask	= null;


    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JourneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        requestButton = (FloatingActionButton)getActivity().findViewById(R.id.requestButton);

        responseStationList = new ArrayList<>();

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConnectionActivity.class);
                // extras
                intent.putExtra("Departure", mDepartureStationAutoCompleteTextView.getText().toString());
                intent.putExtra("Arrival", mDirectionStationAutoCompleteTextView.getText().toString());
                startActivity(intent);
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
