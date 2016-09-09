package be.ontime.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.ontime.Models.Connection;
import be.ontime.Models.Favourite;

/**
 * Created by Jawad on 10-02-16.
 */
public class PrefsUtils {

    /** Boolean indicating whether ToS has been accepted */
    public static final String PREF_TOS_ACCEPTED = "pref_tos_accepted";
    public static final String PREF_TOOLTIPS_A_ACCEPTED = "pref_tooltips_a_accepted";
    public static final String PREF_TOOLTIPS_B_ACCEPTED = "pref_tooltips_b_accepted";

    public static final String PREF_CONNECTIONS = "pref_connections";
    public static final String CONNECTIONS = "Connections";

    public static final String PREF_FAVOURITES = "pref_favourites";
    public static final String FAVOURITES = "Favourites";

    public static boolean isTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_TOS_ACCEPTED, false);
    }
    public static void markTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_TOS_ACCEPTED, true).commit();
    }
    public static void clearConnections(Context context) {
        // used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_CONNECTIONS,Context.MODE_PRIVATE);
        editor = settings.edit();
       // try{
            editor.clear();
            editor.commit();
       // }catch(Exception e){
            System.out.println("Cart Error");
      //  }

    }
    public static void storeConnections(Context context, List connections) {
        // used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_CONNECTIONS,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(connections);
        editor.putString(CONNECTIONS, jsonFavorites);
        editor.commit();
    }
    public static ArrayList loadConnections(Context context) {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List connections;
        settings = context.getSharedPreferences(PREF_CONNECTIONS,Context.MODE_PRIVATE);
        if (settings.contains(CONNECTIONS)) {
            String jsonConnections = settings.getString(CONNECTIONS, null);
            Log.d("jsonConnections : ",jsonConnections);
            Gson gson = new Gson();
            Connection[] connectionItems = gson.fromJson(jsonConnections,Connection[].class);

            Log.d("connectionItems : ",connectionItems.length+"");
            connections = Arrays.asList(connectionItems);
            connections = new ArrayList(connections);
            Log.d("ArrayList size: ",connections.size()+"");
        } else
            return null;
        return (ArrayList) connections;
    }
    public static void addConnection(Context context, Connection connection) {
        List favorites = loadConnections(context);
        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(0,connection);
        storeConnections(context, favorites);
    }
    public static void removeConnection(Context context, int position) {
        List favorites = loadConnections(context);
        if (favorites != null) {
            favorites.remove(position);
             clearConnections(context);
            storeConnections(context, favorites);
        }

    }

    public static void addFavourites(Context context, Favourite favourite) {
        List favourites = loadFavourites(context);
        if (favourites == null)
            favourites = new ArrayList();
        favourites.add(favourite);
        storeFavourites(context, favourites);
    }


   public static ArrayList loadFavourites(Context context) {
            // used for retrieving arraylist from json formatted string
            SharedPreferences settings;
            List favourites;
            settings = context.getSharedPreferences(PREF_FAVOURITES,Context.MODE_PRIVATE);
            if (settings.contains(FAVOURITES)) {
                String jsonFavourites= settings.getString(FAVOURITES, null);
                Log.d("jsonFavourites : ",jsonFavourites);
                Gson gson = new Gson();
                Favourite[] favouritesItems = gson.fromJson(jsonFavourites,Favourite[].class);

                Log.d("favouritesItems : ",favouritesItems.length+"");
                favourites = Arrays.asList(favouritesItems);
                favourites = new ArrayList(favourites);
                Log.d(" size favourites:  ",favourites.size()+"");
            } else
                return null;
            return (ArrayList) favourites;
    }
    public static void removeFavourite(Context context, int position) {
        List favourites = loadFavourites(context);
        if (favourites != null) {
            favourites.remove(position);
            storeFavourites(context, favourites);
        }
    }

    public static void storeFavourites(Context context, List favourites) {
        // used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_FAVOURITES,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favourites);
        editor.putString(FAVOURITES, jsonFavorites);
        editor.commit();
    }
}
