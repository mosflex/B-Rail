package be.ontime.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jawad on 10-02-16.
 */
public class NetworkUtils {



    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static boolean isConnectToInternet(Context context){
        boolean isConnect = false;

        try
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if(mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()){
                HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "TestActivity");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(4000);
                urlc.setReadTimeout(4000);
                urlc.connect();

                isConnect = true;
            }

        } catch (IOException e){
            isConnect = false;
        }

        return isConnect;
    }



}
