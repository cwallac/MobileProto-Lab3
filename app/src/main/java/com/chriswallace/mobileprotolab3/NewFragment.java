package com.chriswallace.mobileprotolab3;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chriswallace.mobileprotolab3.Database.ChatEntry;
import com.chriswallace.mobileprotolab3.Database.FirebaseDatabase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by cwallace on 9/30/14.
 */
public class NewFragment extends Fragment {
    FirebaseDatabase firebaseDatabase = new FirebaseDatabase("https://shining-torch-8136.firebaseio.com/");
    public NewFragment() {
    }

    ArrayList<String> messageList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        final ListView myListView = (ListView) rootView.findViewById(R.id.my_list_view);
        final Button myButton = (Button) rootView.findViewById(R.id.my_button);
        final FirebaseDatabase firebaseDatabase = new FirebaseDatabase("https://shining-torch-8136.firebaseio.com/");


        messageList = new ArrayList<String>();

        //Adapter added on create
        myListView.setAdapter(new ChatAdapter(getActivity(), R.layout.chat_layout,
                messageList));

        firebaseDatabase.myFirebaseRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                //Log.d("Test",snapshot.getPriority().toString());
                String name = newPost.get("name").toString();
                String message = newPost.get("message").toString();
                String timestamp = newPost.get("timestamp").toString();
                messageList.add(name + " : " + timestamp + " : " +message);
                myListView.setAdapter(new ChatAdapter(getActivity(), R.layout.chat_layout,
                        messageList)); ///WHenver Data is added update messagelist, and the adapter
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


        //On Click function for each row of the listview: Passed teh row to the editSelectedTExt Method
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView <?> arg0, View arg1, int arg2,
                                    long arg3) {
                editSelectedText(arg2); //ARG 2 is the index of what was clicked on



            }
        });

// MY BUTTON LISTENER, ADDS TEXT TO DATABASE, ALSO PREVENTS EMPTY LINES FROM BEING ENTERED
        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myButton.setText(R.string.button_text);
                MainActivity activity = (MainActivity)getActivity();
                String date = getDate("MM/dd/yyyy hh:mm:ss");
                EditText editText = (EditText) rootView.findViewById(R.id.text_to_add);
                String stringToAdd =  editText.getText().toString();

                if (stringToAdd.length() == 0 ) {
                    myButton.setText("ADD TEXT");
                }
                else {

                    editText.setText("");
                    firebaseDatabase.PushData(new ChatEntry(activity.username, stringToAdd, date));

                }

            }
        });

        return rootView;
    }

    public void clearText() { //CLEARS THE DATABASE AND THE LISTVIEW

        final ListView myListView = (ListView) getView().findViewById(R.id.my_list_view);
        myListView.setAdapter(null);
        firebaseDatabase.deleteDatabase();
        messageList.clear();

    }
    public void editSelectedText(final int which) { //CALLED WHEN A LISTVIEW ITEM IS CLICKED ON
        //CREATES ALERTDIALOG WITH BUTTONS
        createAlertDialog("Change text", "Accept", "Cancel");
    }
    //REFACTOR TO USE SAME onClick function with cases



    public void change_user_alert() //CALLED FROM MyActivity, CREATES ALERT DIALOG WITH BUTTONS TO CHANGE USERNAME

    {


        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .create();
        ad.setCancelable(false);
        ad.setTitle("Change Username");

        final EditText input = new EditText(getActivity());
        ad.setView(input);
        ad.setButton(-1,"Accept", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                String Value = input.getText().toString();
                MainActivity activity = (MainActivity)getActivity();
                activity.username = Value;
                dialog.dismiss();

            }
        });
        //REFACTOR TO USE SAME onClick function with separate cases is better practice, didn't have time


        ad.setButton(-2,"Cancel", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        ad.show();

    }



    public static String getDate(String dateFormat)
    {
        long milliSeconds = System.currentTimeMillis();
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void createAlertDialog(String title, String PosButton, String NegButton){

        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .create();
        ad.setCancelable(false);
        ad.setTitle(title);

        final EditText input = new EditText(getActivity());
        ad.setView(input);

        ad.setButton(-1,PosButton, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int entry) { //EDIT TEXT IS ACCEPTED WHEN THIS IS CLICKED


                final ListView myListView = (ListView) getView().findViewById(R.id.my_list_view);
                MainActivity activity = (MainActivity)getActivity();
                //MODFY ENTRY IN DATABASE BY PASSING ID TO A FUNCTION WITHIN MODEL DATABASE THEN RELOAD THE DATABASE


                dialog.dismiss();

            }
        });
        //REFACTOR TO USE SAME onClick function with cases


        ad.setButton(-2,NegButton, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });
        ad.show();

    }


}
