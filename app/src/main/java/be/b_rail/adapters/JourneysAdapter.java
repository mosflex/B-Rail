package be.b_rail.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import be.b_rail.MainActivity;
import be.b_rail.Models.Connection;
import be.b_rail.R;
import be.b_rail.Utils.PrefsUtils;
import be.b_rail.Utils.Utils;
import be.b_rail.fragments.JourneyFragment;
import be.b_rail.widget.AnimTextView;

/**
 * Created by Jawad on 29-05-16.
 */
public class JourneysAdapter
        extends RecyclerView.Adapter<JourneysAdapter.ViewHolder> {

    private static List<Connection> mConnectionsList;
    private static Context          context;
    private JourneyFragment     journeyFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTimeDepartureTextView;
        private TextView mTimeArrivalTextView;
        private TextView mStationDepartureTextView;
        private TextView mStationArrivalTextView;
        private TextView mPlatformDepartureTextView;

        private MagicProgressCircle demoMpc;
        private AnimTextView        mAnimTextView_time;

        private Button              btn_remove_journey;


        public ViewHolder(View v) {
            super(v);

            mTimeDepartureTextView      = (TextView) v.findViewById(R.id.time_departure_textview);
            mTimeArrivalTextView        = (TextView) v.findViewById(R.id.time_arrival_textview);
            mStationDepartureTextView   = (TextView) v.findViewById(R.id.station_departure_textview);
            mStationArrivalTextView     = (TextView) v.findViewById(R.id.station_arrival_textview);
            mPlatformDepartureTextView  = (TextView) v.findViewById(R.id.Platform_departure_textview);

            demoMpc             = (MagicProgressCircle) v.findViewById(R.id.demo_mpc);
            mAnimTextView_time  = (AnimTextView) v.findViewById(R.id.animTextView_time);

            btn_remove_journey      = (Button) v.findViewById(R.id.btn_remove);
        }
    }


    public JourneysAdapter(Context mContext, List<Connection> connectionsList,JourneyFragment fragment) {
        context = mContext;
        mConnectionsList = connectionsList;
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
        this.journeyFragment = fragment;

    }
    // Create new views (invoked by the layout manager)
    @Override
    public JourneysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journey, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Connection connection  = mConnectionsList.get(position);

        holder.mStationDepartureTextView.setText(connection.getDeparture().getStation());
        holder.mTimeDepartureTextView.setText(Utils.getTimeFromDate(connection.getDeparture().getTime()));
        holder.mStationArrivalTextView.setText(connection.getArrival().getStation());
        holder.mTimeArrivalTextView.setText(Utils.getTimeFromDate(connection.getArrival().getTime()));
        holder.mPlatformDepartureTextView.setText(connection.getDeparture().getPlatform());
        holder.btn_remove_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsUtils.removeConnection(context, position);
                mConnectionsList.remove(connection);
                if (position == 0) {notifyDataSetChanged(); journeyFragment.showFlip();} else {notifyItemRemoved(position);}
                Snackbar.make(view, "connection supprimé : " + connection.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        long seconds_now = TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis());
        long seconds_departure = Long.parseLong(connection.getDeparture().getTime());// en second
        final int progress = Utils.getDurationTimeSeconds(seconds_departure,seconds_now) ;

        /*Log.d("JourneyAdapter", "DEPART : "+ Utils.getTimeFromDate(connection.getDeparture().getTime()));
        Log.d("JourneyAdapter", "connection.getDeparture().getTime() : " +connection.getDeparture().getTime());
        Log.d("JourneyAdapter", "progress : " + progress);
        Log.d("JourneyAdapter", "progress (HH:mm) : " +Utils.getDurationString(progress));
        Log.d("JourneyAdapter", "seconds_now : " +seconds_now);
        Log.d("JourneyAdapter", "seconds_departure : " +seconds_departure);*/

        final float max = progress;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
            ObjectAnimator.ofFloat(holder.demoMpc, "percent",0, progress/max),
            ObjectAnimator.ofInt(holder.mAnimTextView_time, "progress", progress,0)
        );
        if(progress >= 0){
            set.setDuration(progress*1000);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    //isAnimActive = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // isAnimActive = false;
                    Log.d("onAnimationEnd", "onAnimationEnd" );
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            set.setInterpolator(new AccelerateInterpolator());
            set.start();
        }else{

        }
    }
    @Override
    public long getItemId(int position) {
        return mConnectionsList.get(position).getId();
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
