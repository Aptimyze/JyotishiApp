package com.jyotishapp.jyotishi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver{

    public static ConnectivityChangedListener connectivityChangedListener = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(connectivityChangedListener!=null){
            connectivityChangedListener.onNetworkChangedListener(isConnectedOrConnecting(context));
        }
    }

    boolean isConnectedOrConnecting(Context context){
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnectedOrConnecting();

    }

}
