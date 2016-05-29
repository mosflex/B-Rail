package be.b_rail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.b_rail.Models.Connection;
import be.b_rail.adapters.ConnectionAdapter;

public class ConnectionActivity extends AppCompatActivity {

    private String                      departure, arrival;
    private GetConnectionsJSONTask      getConnectionsJSONTask = null;
    private RecyclerView                connectionListRecycleView;
    private RecyclerView.Adapter        mConnectionAdapter;
    private List<Connection>            responseConnectionList;
    private RecyclerView.LayoutManager  mLayoutManager;

    private TextView                    txt_header_connection;
    private TextView                    txt_header_date;
    private TextView                    txt_header_time;
    private ViewFlipper                 vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.Connection);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        // 1. get passed intent
        Intent intent = getIntent();
        departure   = intent.getStringExtra("Departure");
        arrival     = intent.getStringExtra("Arrival");

        connectionListRecycleView   = (RecyclerView) findViewById(R.id.cardList_connections);
        txt_header_connection       = (TextView) findViewById(R.id.txt_header_connection);
        txt_header_date             = (TextView) findViewById(R.id.txt_header_date);
        txt_header_time             = (TextView) findViewById(R.id.txt_header_time);
        vf                          = (ViewFlipper) findViewById(R.id.viewFlipper);

        //Using one of the built in animations:
        vf.setInAnimation(this, android.R.anim.fade_in);
        vf.setOutAnimation(this, android.R.anim.fade_out);

        responseConnectionList  = new ArrayList<>();
        mLayoutManager          = new LinearLayoutManager(this);
        connectionListRecycleView.setLayoutManager(mLayoutManager);

        getConnectionsJSONTask = new GetConnectionsJSONTask();
        getConnectionsJSONTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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

        private String SERVER_URL =
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
            txt_header_connection.setText(departure +"  >  " + arrival);
            txt_header_date.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            txt_header_time.setText(DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()));
            vf.showNext();

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
