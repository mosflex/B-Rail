package be.b_rail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

import be.b_rail.Models.Station;
import be.b_rail.adapters.StationsAdapter;

public class ScheduleActivity extends AppCompatActivity {

    private LinearLayout            swap_layout;

    private TextInputLayout         inputLayoutDepart, inputLayoutArr;

    private AutoCompleteTextView    mDepartureStationAutoCompleteTextView;
    private AutoCompleteTextView    mDirectionStationAutoCompleteTextView;
    private FloatingActionButton    requestButton;
    private ImageButton             swapButton;

    private List<Station>           responseStationList;
    private StationsAdapter         stationsAdapter;

    private GetStationsJSONTask		getStationsJSONTask	= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.add_a_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        responseStationList = new ArrayList<>();

        swap_layout = (LinearLayout) findViewById(R.id.swap_layout);

        mDepartureStationAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.departure_autoCompleteTextView);
        mDirectionStationAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.direction_autoCompleteTextView);

        inputLayoutDepart = (TextInputLayout) findViewById(R.id.input_layout_depart);
        inputLayoutArr = (TextInputLayout) findViewById(R.id.input_layout_arr);

        mDepartureStationAutoCompleteTextView.addTextChangedListener(new MyTextWatcher(mDepartureStationAutoCompleteTextView));
        mDirectionStationAutoCompleteTextView.addTextChangedListener(new MyTextWatcher(mDirectionStationAutoCompleteTextView));

        swapButton = (ImageButton)findViewById(R.id.swapButton);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                swap_layout.removeViewAt(0);
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
    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateDepart()) {
            return;
        }

        if (!validateArr()) {
            return;
        }

        Intent intent = new Intent(this, ConnectionActivity.class);
        // extras
        intent.putExtra("Departure", mDepartureStationAutoCompleteTextView.getText().toString());
        intent.putExtra("Arrival", mDirectionStationAutoCompleteTextView.getText().toString());
        startActivity(intent);
    }
    private boolean validateDepart() {
        if (mDepartureStationAutoCompleteTextView.getText().toString().trim().isEmpty()) {
            inputLayoutDepart.setError(getString(R.string.err_msg_depart));
            requestFocus(mDepartureStationAutoCompleteTextView);
            return false;
        } else {
            inputLayoutDepart.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateArr() {
        if (mDirectionStationAutoCompleteTextView.getText().toString().trim().isEmpty()) {
            inputLayoutArr.setError(getString(R.string.err_msg_arr));
            requestFocus(mDirectionStationAutoCompleteTextView);
            return false;
        } else {
            inputLayoutArr.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.departure_autoCompleteTextView:
                    validateDepart();
                    break;
                case R.id.direction_autoCompleteTextView:
                    validateArr();
                    break;
            }
        }
    }

    private class GetStationsJSONTask extends AsyncTask<Void, Object, Void> {

        private static final String TAG = "GetStationsTask";
        private static final String SERVER_URL = "stationsfr.json";

        @Override
        protected Void doInBackground(Void... params) {
            BufferedReader reader = null;

            try {

                reader = new BufferedReader(new InputStreamReader(getAssets().open(SERVER_URL)));

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
            stationsAdapter = new StationsAdapter(ScheduleActivity.this,R.layout.item_station,R.id.txtNameStation, responseStationList);
            mDepartureStationAutoCompleteTextView.setAdapter(stationsAdapter);
            mDirectionStationAutoCompleteTextView.setAdapter(stationsAdapter);
        }
    }

}
