package com.example.nottingbot.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * This class is used to check whether the application
 * is connected to Internet
 *
 * @author Junhan LIU
 */
public class ConnectionCheckUtil {
    /**
     * Check whether the application is connected to the internet
     * return true if connected
     * return false if not connected
     *
     * @param context Context object
     * @return boolean value, whether it is connected to Internet
     */
    public static boolean checkConnectNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            isConnected = true;
        }
        else {
            Toast.makeText(context, "Network connection not working...", Toast.LENGTH_LONG).show();
        }
        return isConnected;
    }
}
