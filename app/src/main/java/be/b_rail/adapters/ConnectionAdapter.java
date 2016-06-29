package be.b_rail.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import be.b_rail.MainActivity;
import be.b_rail.Models.Connection;

import be.b_rail.Models.Via;
import be.b_rail.R;
import be.b_rail.Utils.PrefsUtils;
import be.b_rail.Utils.Utils;
import be.b_rail.widget.ExpandableItemIndicator;

/**
 * Created by Jawad & TTM on 15/02/2016.
 */
public class ConnectionAdapter extends AbstractExpandableItemAdapter<ConnectionAdapter.GroupViewHolder,ConnectionAdapter.ChildViewHolder> {

    private static final String TAG = "ConnectionItemAdapter";

    private List<Connection>    mConnectionsList;
    private Context             context;

    private interface Expandable extends ExpandableItemConstants {
    }

    public static abstract class BaseViewHolder extends AbstractExpandableItemViewHolder
            implements ExpandableItemViewHolder {

        public LinearLayout     mContainer;
        private int             mExpandStateFlags;

        public BaseViewHolder(View v) {
            super(v);
            mContainer = (LinearLayout) v.findViewById(R.id.container);
        }
        @Override
        public int getExpandStateFlags() {
            return mExpandStateFlags;
        }

        @Override
        public void setExpandStateFlags(int flag) {
            mExpandStateFlags = flag;
        }

    }

    public static class GroupViewHolder extends BaseViewHolder {

        public ExpandableItemIndicator mIndicator;
        private TextView mTimeDepartureTextView;
        private TextView mTimeArrivalTextView;
        private TextView mDurationTravelTextView;
        private TextView mViasNumberTextView;

        private FloatingActionButton btn_add_journey;

        public GroupViewHolder(View v) {
            super(v);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
            mTimeDepartureTextView = (TextView) v.findViewById(R.id.time_departure_textview);
            mTimeArrivalTextView = (TextView) v.findViewById(R.id.time_arrival_textview);
            mDurationTravelTextView = (TextView) v.findViewById(R.id.duration_travel_textview);
            btn_add_journey = (FloatingActionButton) v.findViewById(R.id.add_journey);
            mViasNumberTextView = (TextView) v.findViewById(R.id.vias_number_textview);
        }
    }

    public static class ChildViewHolder extends BaseViewHolder {

        private TextView mTimeDepartureChildTextView;
        private TextView mTimeArrivalChildTextView;
        private TextView mTimeDepartureViaChildTextView;
        private TextView mTimeArrivalViaChildTextView;

        private TextView mStationDepartureChildTextView;
        private TextView mStationArrivalChildTextView;
        private TextView mStationViaChildTextView;

        private TextView mPlatformDepartureChildTextView;
        private TextView mPlatformArrivalChildTextView;
        private TextView mPlatformViaChildTextView;

        private TextView mDelayChildTextView;

        private LinearLayout mViasChildLinearLayout;


        public ChildViewHolder(View v) {
           super(v);
            mTimeArrivalChildTextView = (TextView) v.findViewById(R.id.time_arrival_textview_child);
            mTimeDepartureChildTextView = (TextView) v.findViewById(R.id.time_departure_textview_child);
            mTimeDepartureViaChildTextView = (TextView) v.findViewById(R.id.time_via_departure_textview_child) ;
            mTimeArrivalViaChildTextView =(TextView)v.findViewById(R.id.time_via_arrival_textview_child);

            mStationDepartureChildTextView = (TextView) v.findViewById(R.id.station_departure_textview_child);
            mStationArrivalChildTextView = (TextView) v.findViewById(R.id.station_arrival_textview_child);
            mStationViaChildTextView = (TextView)v.findViewById(R.id.station_via_textview_child);

            mPlatformDepartureChildTextView = (TextView) v.findViewById(R.id.platform_departure_textview_child);
            mPlatformArrivalChildTextView = (TextView) v.findViewById(R.id.platform_arrival_textview_child);
            mPlatformViaChildTextView = (TextView) v.findViewById(R.id.platform_via_textview_child);

            mViasChildLinearLayout =(LinearLayout) v.findViewById(R.id.vias_linearlayout_child);

            mDelayChildTextView =(TextView) v.findViewById(R.id.time_delay_textview_child);

        }
    }

    public ConnectionAdapter(Context mContext, List<Connection> connectionsList) {
        context = mContext;
        mConnectionsList = connectionsList;

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    public void add(Connection connection) {
        mConnectionsList.add(connection);
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return mConnectionsList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (mConnectionsList.get(groupPosition).getVias() != null) {
            return Integer.parseInt(mConnectionsList.get(groupPosition).getVias().getNumber());
        }
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mConnectionsList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (mConnectionsList.get(groupPosition).getVias()!= null) return mConnectionsList.get(groupPosition).getVias().getVia().get(childPosition).getId();
        return 0;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_connection, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_connection_child, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition, int viewType) {
        final Connection connection = mConnectionsList.get(groupPosition);
        holder.btn_add_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsUtils.addConnection(context, connection);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                Snackbar.make(view, R.string.snackar_journey_added, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
        //holder.mStationDepartureTextView.setText(connection.getDeparture().getStation());
        holder.mTimeDepartureTextView.setText(Utils.getTimeFromDate(connection.getDeparture().getTime()));
        holder.mDurationTravelTextView.setText(Utils.getDurationString(connection.getDuration()));

        //holder.mStationArrivalTextView.setText(connection.getArrival().getStation());
        holder.mTimeArrivalTextView.setText(Utils.getTimeFromDate(connection.getArrival().getTime()));

        if (connection.getVias() != null) {
            holder.mViasNumberTextView.setText(connection.getVias().getNumber() + " correspondance");
        }

        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
       final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            //int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
              //  bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
             //   bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

           // holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);

        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        final Connection connection = mConnectionsList.get(groupPosition);
        ArrayList<Via> mVias;
        holder.mViasChildLinearLayout.setVisibility(View.GONE);
        holder.mTimeDepartureChildTextView.setText(Utils.getTimeFromDate(connection.getDeparture().getTime()));
        holder.mTimeArrivalChildTextView.setText(Utils.getTimeFromDate(connection.getArrival().getTime()));

        holder.mStationDepartureChildTextView.setText(connection.getDeparture().getStation());
        holder.mStationArrivalChildTextView.setText(connection.getArrival().getStation());
        holder.mPlatformDepartureChildTextView.setText(connection.getDeparture().getPlatform());
        holder.mPlatformArrivalChildTextView.setText(connection.getArrival().getPlatform());

        if (connection.getVias() != null) {
            holder.mViasChildLinearLayout.setVisibility(View.VISIBLE);
            mVias = connection.getVias().getVia();
            holder.mTimeArrivalViaChildTextView.setText(Utils.getTimeFromDate(mVias.get(childPosition).getArrival().getTime()));
            holder.mTimeDepartureViaChildTextView.setText(Utils.getTimeFromDate(mVias.get(childPosition).getDeparture().getTime()));

            holder.mStationViaChildTextView.setText(mVias.get(childPosition).getStation());
            holder.mPlatformViaChildTextView.setText(mVias.get(childPosition).getDeparture().getPlatform());
         }

        if (connection.getDeparture().getDelay() > 0){
            holder.mDelayChildTextView.setText("+ "+connection.getDeparture().getDelay()/60+"'");
        }
        //holder.mDirectionChildTextView.setText(connection.getDeparture().getVehicle().substring(8)+""+connection.getDeparture().getDirection().getName());

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned
        return true;
    }
}



