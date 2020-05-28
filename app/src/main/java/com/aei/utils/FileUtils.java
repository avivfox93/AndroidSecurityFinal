package com.aei.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    public static List<File> scanFiles(File parent){
        ArrayList<File> inFiles = new ArrayList<>(); // initialize an array list to store file names
        File[] files = parent.listFiles(); // list all files in this directory
        if(files != null) {
            for (File file : files) {
                if(file.getName().startsWith("."))
                    continue;
                if (file.isDirectory()) { // if the file is a directory
                    inFiles.addAll(scanFiles(file)); // **CALL THIS RECURSIVELY TO GET ALL LOWER LEVEL FILES**
                }else{
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public static String fileListToJson(List<File> files){
        List<String> paths = files.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        return new Gson().toJson(paths);
    }
}
