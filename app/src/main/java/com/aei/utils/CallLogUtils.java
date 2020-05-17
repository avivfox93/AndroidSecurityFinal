package com.aei.utils;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogUtils {
    static class Call{
        private int type;
        private String number;
        private String duration;
        private Date date;

        public Call(int type, String number, String duration, Date date) {
            this.type = type;
            this.number = number;
            this.duration = duration;
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }

    public static List<Call> getCallLog(Application cntx){
        List<Call> calls = new ArrayList<>();
        Uri allCalls = Uri.parse("content://call_log/calls");
        Cursor c = cntx.getContentResolver().query(allCalls, null, null, null, null);
        if(c == null)
            return calls;
        int number = c.getColumnIndex(CallLog.Calls.NUMBER);
        int type = c.getColumnIndex(CallLog.Calls.TYPE);
        int date = c.getColumnIndex(CallLog.Calls.DATE);
        int duration = c.getColumnIndex(CallLog.Calls.DURATION);
        while(c.moveToNext()){
            String phNumber = c.getString(number);
            String callType = c.getString(type);
            String callDate = c.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = c.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            //Call(int type, String number, long duration, String name, Date date)
            calls.add(new Call(dircode,phNumber,callDuration,callDayTime));
        }
        return calls;
    }
}
