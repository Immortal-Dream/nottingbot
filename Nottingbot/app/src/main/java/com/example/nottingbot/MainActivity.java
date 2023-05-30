package com.example.nottingbot;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


/**
 * Default entry point activity of an Android app.
 *
 * @author Junhan LIU
 */
public class MainActivity extends AppCompatActivity {


    /**
     * Called when the activity is starting. This is where most initialization should go: calling
     * setContentView(int) to inflate the activity's UI, using findViewById to programmatically interact
     * with widgets in the UI, and using saved instance state if necessary.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}