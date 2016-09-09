package be.ontime;

import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

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

import be.ontime.Models.Connection;
import be.ontime.adapters.ConnectionAdapter;

public class ConnectionActivity extends AppCompatActivity
        implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {


    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private String                      departure, arrival;
    private GetConnectionsJSONTask      getConnectionsJSONTask = null;
    private RecyclerView                connectionListRecycleView;
    private RecyclerView.Adapter        mConnectionAdapter;
    private List<Connection>            responseConnectionList;
    private RecyclerView.LayoutManager  mLayoutManager;

    private RecyclerView.Adapter                mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

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

        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (connectionListRecycleView != null) {
            connectionListRecycleView.setItemAnimator(null);
            connectionListRecycleView.setAdapter(null);
            connectionListRecycleView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mConnectionAdapter = null;
        mLayoutManager = null;
        super.onDestroy();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {

        // NOTE: fromUser is false because explicitly calling the
        // RecyclerViewExpandableItemManager.expand() method in adapter
        adjustScrollPositionOnGroupExpanded(groupPosition);
    }
    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getResources().getDimensionPixelSize(R.dimen.list_child_item_height);
        int topMargin = (int) (getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }
    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
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
            mConnectionAdapter = new ConnectionAdapter(ConnectionActivity.this,responseConnectionList);

            mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(mConnectionAdapter);       // wrap for expanding

            final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

            // Change animations are enabled by default since support-v7-recyclerview v22.
            // Disable the change animation in order to make turning back animation of swiped item works properly.
            // Also need to disable them when using animation indicator.
            animator.setSupportsChangeAnimations(false);

            // requires *wrapped* adapter
            connectionListRecycleView.setAdapter(mWrappedAdapter);
            connectionListRecycleView.setItemAnimator(animator);
            connectionListRecycleView.setHasFixedSize(false);

            // additional decorations
            //noinspection StatementWithEmptyBody
            if (supportsViewElevation()) {
                // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
            } else {
                connectionListRecycleView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(ConnectionActivity.this, R.drawable.material_shadow_z1)));
            }
            connectionListRecycleView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(ConnectionActivity.this, R.drawable.list_divider_h), true));

            mRecyclerViewExpandableItemManager.attachRecyclerView(connectionListRecycleView);

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
