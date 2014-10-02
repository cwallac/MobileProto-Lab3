package com.chriswallace.mobileprotolab3;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;



public class MainActivity extends Activity {

    public String username = "default"; //STARTING THE APP WITH A DEFAULT USER LOGGED IN AND MAKING IT A PROPERTY OF ACTIVITY

    public MainActivity() {
        this.username = "default";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new NewFragment(), "Frag")
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) { // ACTIONS RELATED TO ACTION BAR UP TOP
        // Handle presses on the action bar items
        NewFragment frag = (NewFragment) getFragmentManager().findFragmentByTag("Frag"); //RESOLVING STATIC ISSUE
        switch (item.getItemId()) {

            case R.id.action_clear:

                frag.clearText();

                return true;
            case R.id.change_username:


                frag.change_user_alert(); //CREATE DIALOG FOR USER PROMPT


            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
