package com.aei.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aei.finalproject.MyApp;
import com.aei.utils.MySharedPrefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseConnector {

    public interface CommandReceivedCallback{
        void onReceived(Command command);
    }

    private MySharedPrefs mySharedPrefs;

    public DatabaseConnector(Context context){
        mySharedPrefs = new MySharedPrefs(context);
    }

    public void sendCommand(Command command, String id){
        FirebaseDatabase.getInstance().getReference("devices").child(id).child("command")
                .setValue(command);
    }

    public void addOnCommandReceivedCallback(CommandReceivedCallback callback){
        FirebaseDatabase.getInstance().getReference("devices")
                .child(mySharedPrefs.getString("ID","Unknown"))
                .child("command")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) return;
                        callback.onReceived(dataSnapshot.getValue(Command.class));
                        dataSnapshot.getRef().setValue(null);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void sendResponse(Command command){
        FirebaseDatabase.getInstance().getReference("devices")
                .child(mySharedPrefs.getString("ID","Unknown"))
                .child("response").setValue(command);
    }

    public void addOnResponse(CommandReceivedCallback callback){
        FirebaseDatabase.getInstance().getReference("devices")
                .child(mySharedPrefs.getString("ID","Unknown"))
                .child("response")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) return;
                        callback.onReceived(dataSnapshot.getValue(Command.class));
                        dataSnapshot.getRef().setValue("");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
