package be.b_rail.adapters;

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
    int resource, textViewResourceId;
    List<Station> items, tempItems, suggestions;


    public StationsAdapter(Context context, int resource, int textViewResourceId, List<Station> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    public void add(Station station){
        tempItems.add(station);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_station, parent, false);
        }
        Station station = items.get(position);
        if (station != null) {
            TextView lblName = (TextView) view.findViewById(R.id.txtNameStation);
            if (lblName != null)
                lblName.setText(station.getName());
        }
        return view;
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

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Station station : tempItems) {
                    if (station.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(station);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Station> filterList = (ArrayList<Station>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Station station : filterList) {
                    add(station);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
