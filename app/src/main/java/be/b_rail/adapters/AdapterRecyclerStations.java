package be.b_rail.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import be.b_rail.Models.Station;
import be.b_rail.R;

public class AdapterRecyclerStations extends RecyclerView.Adapter<AdapterRecyclerStations.ViewHolder>{

    private static ArrayList<Station>   mDataset;
    private static Context              mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mContainer;
        public TextView     txtNameStation;
        public TextView     txtIdStation;

        public ViewHolder(View itemView) {
            super(itemView);
            mContainer      = (LinearLayout) itemView.findViewById(R.id.container);
            txtNameStation  = (TextView) itemView.findViewById(R.id.txtNameStation);
            txtIdStation    = (TextView) itemView.findViewById(R.id.txtIdStation);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        }
        return 0;
    }

    public void add(Station item) {
        mDataset.add(item);
        notifyDataSetChanged();
    }


    public void remove(Station item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerStations(Context context) {
        mDataset = new ArrayList<>();
        mContext = context;

        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_station, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtNameStation.setText(mDataset.get(position).getName());
        holder.txtIdStation.setText(mDataset.get(position).getIdStation()+"");

    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position).getId();
    }
}