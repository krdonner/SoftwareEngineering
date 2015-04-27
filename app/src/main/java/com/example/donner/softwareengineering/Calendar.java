package com.example.donner.softwareengineering;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class Calendar extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        GridView calendarGridView = (GridView)findViewById(R.id.gridView);
        calendarGridView.setAdapter(new ImageAdapterForCalendar(this));

        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Calendar.this, ""+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Calendar.this, DayOverview.class);
                startActivity(intent);
            }
        });

        calendarGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Calendar.this, ""+position, Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

}
