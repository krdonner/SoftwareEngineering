package com.example.donner.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by Donner on 2015-05-18.
 */
public class AddEvent extends ActionBarActivity{

    TimePicker myTimePicker;
    Button button;
    EditText eventText;
    int hour;
    int minute;
    String event;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);
        myTimePicker = (TimePicker)findViewById(R.id.timePicker);
        button = (Button)findViewById(R.id.saveButton);
        eventText = (EditText)findViewById(R.id.eventTextField);
        myTimePicker.setIs24HourView(true);
        saveInformation();

    }

    public void saveInformation(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = myTimePicker.getCurrentHour();
                minute = myTimePicker.getCurrentMinute();
                event = eventText.getText().toString();

                Intent intent = new Intent(AddEvent.this, MyCalendarActivity.class);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.putExtra("event", event);
                startActivity(intent);


            }
        });



    }
}
