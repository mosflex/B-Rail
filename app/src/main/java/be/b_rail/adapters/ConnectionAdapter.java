package be.b_rail.adapters;

import android.content.Context;
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


import java.util.List;

import be.b_rail.Models.Connection;
import be.b_rail.R;
import be.b_rail.Utils.PrefsUtils;
import be.b_rail.Utils.Utils;
import be.b_rail.widget.ExpandableItemIndicator;

/**
 * Created by Jawad & TTM on 15/02/2016.
 */
public class ConnectionAdapter extends AbstractExpandableItemAdapter<ConnectionAdapter.GroupViewHolder,ConnectionAdapter.ChildViewHolder> {


    private List<Connection> mConnectionsList;
    private Context context;

    private static RecyclerViewExpandableItemManager mExpandableItemManager;

    private interface Expandable extends ExpandableItemConstants {
    }

    public static abstract class BaseViewHolder extends AbstractExpandableItemViewHolder
            implements ExpandableItemViewHolder {

        public LinearLayout mContainer;
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
        public ChildViewHolder(View v) {
            super(v);
        }
    }

    public ConnectionAdapter(Context mContext, List<Connection> connectionsList, RecyclerViewExpandableItemManager expandableItemManager) {
        context = mContext;
        mExpandableItemManager = expandableItemManager;
        mConnectionsList = connectionsList;

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    public void add(Connection connection) {
        mConnectionsList.add(connection);
        notifyDataSetChanged();

    }

    private static final String TAG = "ConnectionItemAdapter";

    @Override
    public int getGroupCount() {
        return mConnectionsList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (mConnectionsList.get(groupPosition).getVias() != null) {

            return Integer.parseInt(mConnectionsList.get(groupPosition).getVias().getNumber());
        }
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mConnectionsList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition + childPosition;
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
                Snackbar.make(view, "connection ajouté : " + connection.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned
        return false;
    }
}



