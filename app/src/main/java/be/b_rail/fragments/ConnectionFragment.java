package be.b_rail.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

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
import be.b_rail.R;
import be.b_rail.adapters.ConnectionAdapter;

/**
 * Created by TTM on 15/02/2016.
 */
public class ConnectionFragment extends BaseFragment {

    private String departure, arrival;
    private GetConnectionsJSONTask  getConnectionsJSONTask = null;
    private TextView responseTextView2;
    private RecyclerView connectionListRecycleView;
    private RecyclerView.Adapter mConnectionAdapter;
    private List<Connection>  responseConnectionList;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    public int getTitleResourceId() {
        return R.string.Connection;
    }

    public static ConnectionFragment newInstance() {
        return new ConnectionFragment();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InputMethodManager inputManager = (InputMethodManager)
               getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_connection, container, false);


        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        departure = bundle.getString("Departure");
        arrival = bundle.getString("Arrival");

        getConnectionsJSONTask = new GetConnectionsJSONTask();
        getConnectionsJSONTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        responseConnectionList = new ArrayList<>();
        connectionListRecycleView = (RecyclerView) rootView.findViewById(R.id.cardList_connections);
//        connectionListRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());

        connectionListRecycleView.setLayoutManager(mLayoutManager);



        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        try{
             if(getConnectionsJSONTask != null)getConnectionsJSONTask.cancel(true);
        }catch(IllegalStateException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    /*************************************************************************************************************************/
    private class GetConnectionsJSONTask extends AsyncTask<Void, Object, Void> {
        private static final String TAG = "GetConnectionsJSONTask";

        private  String SERVER_URL =
                "http://api.irail.be/connections/?to="
                        + arrival+"&from="
                        + departure+"&format=json&fast=true";

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            SERVER_URL = SERVER_URL.replace(" ", "%20");
            try {
                URL url = new URL(SERVER_URL);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Content-type", "application/json");
                conn.connect();

                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line =  "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                Log.i("finalJson : ", finalJson);


                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("connection");

                Gson gson = new Gson();
                for(int i=0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Connection connection = gson.fromJson(finalObject.toString(), Connection.class);

                    responseConnectionList.add(connection);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    conn.disconnect();
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
        protected void onPostExecute(Void aVoid) {
            mConnectionAdapter = new ConnectionAdapter(responseConnectionList);
            connectionListRecycleView.setAdapter(mConnectionAdapter);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
//            Connection connection = (Connection)values[0];
////            responseTextView2.setText("Connection : "+c.getId() + c.getDuration());
//            responseConnectionList.add(connection);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
