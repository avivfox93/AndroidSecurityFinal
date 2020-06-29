package com.aei.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.content.ContextCompat;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context);
    }

    public void startService(Context context) {
        Intent serviceIntent = new Intent(context, MainService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        context.startForegroundService(serviceIntent);
    }
}
