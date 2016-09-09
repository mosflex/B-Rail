package be.ontime.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import be.ontime.ConnectionActivity;
import be.ontime.Models.Favourite;
import be.ontime.R;
import be.ontime.Utils.PrefsUtils;
import be.ontime.fragments.FavouritesFragment;

/**
 * Created by Moshab on 26-06-16.
 */
public class FavouritesAdapter  extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder>  {
    private static List<Favourite>  mFavouriteList;
    private static Context          context;
    private FavouritesFragment      favouriteFragment;

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FavouritesAdapter.ViewHolder holder, final int position) {
        final Favourite favourite  = mFavouriteList.get(position);

        holder.mFavouriteDepartureStationTextView.setText(favourite.getDeparture());
        holder.mFavouriteArrivalStationTextView.setText(favourite.getArrival());

        holder.btn_remove_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsUtils.removeFavourite(context, position);
                mFavouriteList.remove(favourite);
                if (mFavouriteList.size() == 0) {
                    notifyDataSetChanged();
                    favouriteFragment.showFlip();
                } else {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mFavouriteList.size());
                }

                Snackbar.make(view, R.string.snackar_favourite_deleted, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ConnectionActivity.class);
                // extras
                intent.putExtra("Departure", favourite.getDeparture());
                intent.putExtra("Arrival", favourite.getArrival());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavouriteList.size();
    }

    public FavouritesAdapter( Context mContext, List<Favourite> favouriteList,FavouritesFragment fragment) {
        context = mContext;
        mFavouriteList = favouriteList;
        setHasStableIds(true);
        this.favouriteFragment = fragment;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView        mFavouriteDepartureStationTextView;
        private TextView        mFavouriteArrivalStationTextView;
        private LinearLayout    mContainer;
        private ImageButton          btn_remove_favourite;

        public ViewHolder(View v) {
            super(v);
            mFavouriteDepartureStationTextView =(TextView) v.findViewById(R.id.departure_favourite_textview);
            mFavouriteArrivalStationTextView = (TextView) v.findViewById(R.id.arrival_favourite_textview);
            mContainer = (LinearLayout) v.findViewById(R.id.container);

            btn_remove_favourite      = (ImageButton) v.findViewById(R.id.btn_remove);
        }
    }
}
