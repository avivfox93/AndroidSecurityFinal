package com.aei.finalproject;

import android.app.Application;

import com.aei.firebaseutils.storage.StorageUtils;

public class MyApp extends Application {
    private static StorageUtils storageUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        storageUtils = new StorageUtils();
    }

    public StorageUtils getStorage(){
        return storageUtils;
    }

}
