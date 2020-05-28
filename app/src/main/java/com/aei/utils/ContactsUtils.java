package com.aei.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtils {
    static class Contact{
        private String name;
        private List<String> phoneNumber;

        public Contact(){
            phoneNumber = new ArrayList<>();
        }

        public Contact(String name, String email, List<String> phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(List<String> phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
    private Context activity;
    public ContactsUtils(Context activity){
        this.activity = activity;
    }

    public List<Contact> getAllContacts(){
        List<Contact> result = new ArrayList<>();
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                Contact contact = new Contact();
                contact.setName(name);
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.getPhoneNumber().add(phoneNo);
                    }
                    pCur.close();
                }
                result.add(contact);
            }
        }
        if(cur != null)
            cur.close();
        return result;
    }
}
