package be.b_rail.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import be.b_rail.Models.Connection;
import be.b_rail.R;

/**
 * Created by TTM on 15/02/2016.
 */
public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder>{

    private List<Connection> mConnectionsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mDurationTextView;

        public ViewHolder(View v) {

            super(v);
            mDurationTextView = (TextView) v.findViewById(R.id.info_text) ;
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
        holder.mDurationTextView.setText(String.valueOf(connection.getDuration()));

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
