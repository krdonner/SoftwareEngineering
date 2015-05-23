package com.example.donner.softwareengineering;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Donner on 2015-05-18.
 */
public class AddEvent extends ActionBarActivity{

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";
    TimePicker myTimePicker;
    DatePicker myDatePicker;
    Button button;
    EditText edActivity;
    EditText edNotes;
    int hour;
    int minute;
    int time;
    private static String hourMinute;
    private static String act;
    private static String note;
    private Statement state;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);
        myTimePicker = (TimePicker)findViewById(R.id.timePicker);
        myDatePicker = (DatePicker)findViewById(R.id.datePicker);
        button = (Button)findViewById(R.id.saveButton);
        edActivity = (EditText)findViewById(R.id.activity);
        edNotes = (EditText)findViewById(R.id.notes);
        myTimePicker.setIs24HourView(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = myTimePicker.getCurrentHour();
                minute = myTimePicker.getCurrentMinute();
                hourMinute = Integer.toString(hour)+Integer.toString(minute);
                time = Integer.parseInt(hourMinute);
                act = edActivity.getText().toString();
                note = edNotes.getText().toString();


                LoginAsync login = new LoginAsync();
                login.execute(getApplicationContext());



            }
        });

    }

    private class LoginAsync extends AsyncTask<Object, Object, Cursor> {

        String user;
        String pass;



        @Override
        protected Cursor doInBackground(Object... params) {

            try {
                storeInDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }





        private String storeInDB() throws SQLException {

            Connection dbConnection;
            String returnValue = null;

            String insertTableSQL = "INSERT INTO calendar"
                    + "(username, activity, notes, begins, ends, location, date) VALUES"
                    + "('hassel'"+","+ "'"+act+"'"+","+ "'"+note+"'"+","+hour+","+ "16"+"," +"'"+"malmo"+"'"+","+ "20150519"+")";

            try {
                Log.d("test", "1");
                dbConnection = getDBConnection();
                Log.d("test", "1");
                setStatement(dbConnection.createStatement());
                Log.d("test", "1");
                getStatement().executeUpdate(insertTableSQL);
                Log.d("test", "1");



            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return returnValue;
        }

        private Connection getDBConnection() {

            Connection dbConnection = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            try {
                dbConnection = DriverManager.getConnection(
                        "jdbc:mysql://89.160.102.7:3306/projekt" + "?user=" + "ruut"
                + "&password=" + "rooot");
                return dbConnection;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return dbConnection;
        }
    }

    public Statement getStatement() {
        return this.state;
    }

    public void setStatement(Statement state) {
        this.state = state;
    }

}
