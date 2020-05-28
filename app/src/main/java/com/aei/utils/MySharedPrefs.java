package com.aei.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPrefs {

    private SharedPreferences prefs;

    public MySharedPrefs(Context cntx){
        prefs = cntx.getSharedPreferences("myApp",MODE_PRIVATE);
    }

    public void putString(String key, String value){
        prefs.edit().putString(key,value).apply();
    }
    public void putInt(String key, int value){
        prefs.edit().putInt(key,value).apply();
    }
    public void putBoolean(String key, boolean value){
        prefs.edit().putBoolean(key,value).apply();
    }
    public String getString(String key, String def){
        return prefs.getString(key,def);
    }
    public int getInt(String key, int def){
        return prefs.getInt(key,def);
    }
    public boolean getBoolean(String key, boolean def){
        return prefs.getBoolean(key,def);
    }
    public void removeKey(String key){
        prefs.edit().remove(key).apply();
    }
}
