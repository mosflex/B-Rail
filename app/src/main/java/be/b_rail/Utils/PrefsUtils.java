package be.b_rail.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.b_rail.Models.Connection;

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

    public static boolean isTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_TOS_ACCEPTED, false);
    }
    public static void markTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_TOS_ACCEPTED, true).commit();
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
        favorites.add(connection);
        storeConnections(context, favorites);
    }
    public static void removeConnection(Context context, Connection connection) {
        ArrayList favorites = loadConnections(context);
        if (favorites != null) {
            favorites.remove(connection);
            storeConnections(context, favorites);
        }
    }

}
