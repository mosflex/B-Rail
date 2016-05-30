package be.b_rail.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import be.b_rail.Models.Connection;
import be.b_rail.R;
import be.b_rail.Utils.PrefsUtils;
import be.b_rail.Utils.Utils;

/**
 * Created by Jawad & TTM on 15/02/2016.
 */
public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder>{

    private List<Connection> mConnectionsList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTimeDepartureTextView;
        private TextView mTimeArrivalTextView;
        private TextView mDurationTravelTextView;
        private TextView mViasNumberTextView;

        private FloatingActionButton btn_add_journey;

        public ViewHolder(View v) {

            super(v);
            mTimeDepartureTextView  = (TextView) v.findViewById(R.id.time_departure_textview);
            mTimeArrivalTextView    = (TextView) v.findViewById(R.id.time_arrival_textview);
            mDurationTravelTextView = (TextView) v.findViewById(R.id.duration_travel_textview);
            btn_add_journey         = (FloatingActionButton) v.findViewById(R.id.add_journey);
            mViasNumberTextView     = (TextView) v.findViewById(R.id.vias_number_textview);
        }
    }

    public ConnectionAdapter(Context mContext, List<Connection> connectionsList) {
        context = mContext;
        mConnectionsList = connectionsList;
    }

    public void add(Connection connection){
        mConnectionsList.add(connection);
        notifyDataSetChanged();

    }
    // Create new views (invoked by the layout manager)
    @Override
    public ConnectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        Context context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connection, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Connection connection  = mConnectionsList.get(position);
        holder.btn_add_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrefsUtils.addConnection(context,connection);
                Snackbar.make(view, "connection ajouté : " + connection.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //holder.mStationDepartureTextView.setText(connection.getDeparture().getStation());
        holder.mTimeDepartureTextView.setText(Utils.getTimeFromDate(connection.getDeparture().getTime()));
        holder.mDurationTravelTextView.setText(Utils.getDurationString(connection.getDuration()));

        //holder.mStationArrivalTextView.setText(connection.getArrival().getStation());
        holder.mTimeArrivalTextView.setText(Utils.getTimeFromDate(connection.getArrival().getTime()));

        if(connection.getVias()!=null){
           holder.mViasNumberTextView.setText(connection.getVias().getNumber() + " correspondance");
        }
    }

    public Connection getItem(int position) {
        return mConnectionsList.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mConnectionsList.size();
    }
}