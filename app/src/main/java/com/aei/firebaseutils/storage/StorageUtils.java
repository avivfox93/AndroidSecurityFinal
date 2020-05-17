package com.aei.firebaseutils.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class StorageUtils {
    private StorageReference mStorageRef;
    public interface StorageUtilsDownloadCallback{
        void onFileReady(File file);
    }
    public interface StorageUtilsUploadCallback{
        void onFinish(String url);
    }
    public StorageUtils(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    void downloadFile(File to, StorageUtilsDownloadCallback callback){
        mStorageRef.getFile(to)
                .addOnSuccessListener(taskSnapshot -> callback.onFileReady(to))
                .addOnFailureListener(e -> callback.onFileReady(null));
    }
    void uploadFile(File file, String path, StorageUtilsUploadCallback callback){
        StorageReference ref = mStorageRef
                .child(path + file.getName());
        Uri uri = Uri.fromFile(file);
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                .addOnSuccessListener(result->callback.onFinish(result.toString())));
    }
}
