package com.dev.muslim.pantaumeter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.AppBarLayout;

/**
 * Created by owner on 7/30/2017.
 */

public class GetConnection {
    public static String IP="http://192.168.230.32:8080";
//    public static String IP="http://192.168.43.116:8080";
//     public static String IP="http://10.0.3.2:8080";
//    public static String IP;


    private static GetConnection instance = null;

    public static GetConnection getInstance() {
        if(instance == null) {
            instance = new GetConnection();
        }
        return instance;
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
