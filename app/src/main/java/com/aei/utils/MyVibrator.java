package com.aei.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class MyVibrator {
    private Vibrator vibrator;
    public MyVibrator(Context cntx){
        vibrator = (Vibrator)cntx.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate(int duration){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        else vibrator.vibrate(duration);
    }
}
