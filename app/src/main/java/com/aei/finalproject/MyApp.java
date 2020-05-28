package com.aei.finalproject;

import android.app.Application;

import com.aei.firebaseutils.storage.StorageUtils;
import com.aei.managment.CommandInvoker;
import com.aei.utils.MySharedPrefs;

public class MyApp extends Application {
    private static StorageUtils storageUtils;
    private static CommandInvoker invoker;
    private static MySharedPrefs mySharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        storageUtils = new StorageUtils();
        invoker = new CommandInvoker(this);
        mySharedPrefs = new MySharedPrefs(this);
    }

    public static MySharedPrefs getMySharedPrefs(){return mySharedPrefs;}

    public static CommandInvoker getInvoker() {
        return invoker;
    }

    public static StorageUtils getStorage(){
        return storageUtils;
    }

}
