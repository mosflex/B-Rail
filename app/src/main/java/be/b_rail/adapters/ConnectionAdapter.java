package be.b_rail.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.b_rail.Models.Connection;
import be.b_rail.R;
import be.b_rail.Utils.Utils;

/**
 * Created by TTM on 15/02/2016.
 */
public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder>{

    private List<Connection> mConnectionsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView mTimeDepartureTextView;
        private TextView mTimeArrivalTextView;
        private TextView mDurationTravelTextView;
        private TextView mStationDepartureTextView;
        private TextView mStationArrivalTextView;

        public ViewHolder(View v) {

            super(v);
            mTimeDepartureTextView =(TextView) v.findViewById(R.id.time_departure_textview);
            mTimeArrivalTextView =(TextView) v.findViewById(R.id.time_arrival_textview);
            mDurationTravelTextView = (TextView) v.findViewById(R.id.duration_travel_textview) ;
            mStationDepartureTextView =(TextView)v.findViewById(R.id.station_departure_textview);
            mStationArrivalTextView =(TextView)v.findViewById(R.id.station_arrival_textview);
        }
    }


    public ConnectionAdapter(List<Connection> connectionsList) {
        mConnectionsList = connectionsList;

    }

    public void add(Connection connection){
        mConnectionsList.add(connection);
        notifyDataSetChanged();

    }
    // Create new views (invoked by the layout manager)
    @Override
    public ConnectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        Context context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_connection, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Connection connection  = mConnectionsList.get(position);

            holder.mTimeDepartureTextView.setText(Utils.getTimeFromDate(connection.getDeparture().getTime()));
            holder.mTimeArrivalTextView.setText(Utils.getTimeFromDate(connection.getArrival().getTime()));
            holder.mDurationTravelTextView.setText(Utils.getDurationString(connection.getDuration()));
            holder.mStationDepartureTextView.setText(connection.getDeparture().getStation());
            holder.mStationArrivalTextView.setText(connection.getArrival().getStation());

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
