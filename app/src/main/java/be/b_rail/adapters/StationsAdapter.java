package be.b_rail.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.b_rail.Models.Station;
import be.b_rail.R;

/**
 * Created by Jawad on 11-02-16.
 */
public class StationsAdapter extends ArrayAdapter<Station> {

    Context context;
    int resource, textViewResourceId, count_filter;
    List<Station> items, suggestions;


    public StationsAdapter(Context context, int resource, int textViewResourceId, List<Station> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = new ArrayList<>(items); // this makes the difference.
        this.suggestions = new ArrayList<>(items);
    }

    public void add(Station station){
        items.add(station);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_station, parent, false);
        }
        Station station = suggestions.get(position);
        if (station != null) {
            TextView lblName = (TextView) view.findViewById(R.id.txtNameStation);
            if (lblName != null)
                lblName.setText(station.getName());
        }
        return view;
    }


    @Override
    public int getCount() {
        return count_filter;
    }

    @Override
    public long getItemId(int position) {
        return suggestions.get(position).get_Id();
    }

    @Override
    public Station getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Station) resultValue).getName();
            return str;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.toString().length() > 0){
                constraint = constraint.toString().toLowerCase();
                ArrayList<Station> filteredItems = new ArrayList<>();

                for(int i = 0, l = items.size(); i < l; i++){
                    Station itemStation = items.get(i);

                    if(itemStation.getName().toLowerCase().contains(constraint)){
                        filteredItems.add(itemStation);
                    }
                }
                results.count  = filteredItems.size();
                results.values = filteredItems;

            }else{
                synchronized(this)
                {
                    results.values = items;
                    results.count  = items.size();
                }
            }
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            count_filter = results.count;
            suggestions = (ArrayList<Station>)results.values;
            notifyDataSetChanged();
        }
    };
}
