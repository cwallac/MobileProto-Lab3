package com.chriswallace.mobileprotolab3.Database;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cwallace on 9/25/14.
 */
//https://shining-torch-8136.firebaseio.com/
public class FirebaseDatabase{
    public String URL;
    public Firebase myFirebaseRef;
    public ArrayList<ChatEntry> data;

    public FirebaseDatabase(String url) {
        this.URL = url;
        this.myFirebaseRef = new Firebase(URL);
        this.data = new ArrayList<ChatEntry>();
        myFirebaseRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                Log.d("Author: ", newPost.get("name").toString());
                Log.d("DONE", "DONE");
                String name = newPost.get("name").toString();
                String message = newPost.get("message").toString();
                String timestamp = newPost.get("timestamp").toString();
                data.add(new ChatEntry(name,message,timestamp));
                Log.d("Log",data.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The updated post title is " + title);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String cheddat) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.d("ERROR", "ERROE");
            }

        });


    }
    public ArrayList<ChatEntry> GetAllData(){
        if (data.size() == 0) {

        }

        Log.d("TESTING",data.toString());
        return this.data;
    }

    public Boolean PushData(ChatEntry chat){
        Map<String,String> messages = new HashMap<String, String>();
        messages.put("name",chat.user);
        messages.put("message",chat.Message);
        messages.put("timestamp",chat.Date);

        myFirebaseRef.push().setValue(messages); //MIGHT DELETE IT?

        return true;
    }

    public void deleteDatabase(){
        myFirebaseRef.removeValue();
    }



}
