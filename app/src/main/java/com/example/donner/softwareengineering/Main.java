package com.example.donner.softwareengineering;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent(this,MyCalendarActivity.class);
        startActivity(intent);
    }

    int asdsdasdd = 0; //change
    int sadsadasdas = 0;
    int asffsdgfsdgsd = 0;
    String hej = "hej";



}
