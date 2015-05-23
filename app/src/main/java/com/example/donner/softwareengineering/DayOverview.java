package com.example.donner.softwareengineering;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;


public class DayOverview extends Activity {

    ListView listView;
    String date;
    String theDay;
    int dateInteger;
    static int theDayInteger;
    static int dateFromDB;
    static int beginsFromDB;
    static String notesFromDB;
    static int endsFromDB;
    static String locationFromDB;
    static String activityFromDB;
    String[] values;
    private Statement state;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dayoverview);
        Bundle myBundle = getIntent().getExtras();
        if(myBundle!= null){
            date = myBundle.getString("date");
            theDay = myBundle.getString("theDay");


        }
        dateInteger = Integer.parseInt(date);
        theDayInteger = Integer.parseInt(theDay);
        listView = (ListView) findViewById(R.id.listView);

        LoginAsync loginAs = new LoginAsync();
        loginAs.execute();

        values = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17",
                "18","19","20","21","22","23"};
        System.out.print(dateInteger+ "   "+dateFromDB);





    }

    private class LoginAsync extends AsyncTask<Object, Object, Cursor> {

        String user;
        String pass;


        @Override
        protected Cursor doInBackground(Object... params) {

            try {
                getDate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            Log.e("testar", dateInteger+" "+ dateFromDB);

            if(dateInteger == dateFromDB){

                values[beginsFromDB] += " " + activityFromDB + " " + " " + locationFromDB;


            }




            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2, android.R.id.text2, values);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    int itemPosition = position;
                    String itemValue = (String) listView.getItemAtPosition(position);


                }

            });
        }
    }

        private int getDate() throws SQLException {
            Log.d("hej", "test1");
            Connection dbConnection;
            int returnDate = 0;
            int returnBegins = 0;


            String selectSQL = "SELECT activity, date, begins, notes, ends, location FROM calendar WHERE username = 'hassel' and date = "+dateInteger;
            Log.d("hej", "test2");
            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();

                ResultSet rs = st.executeQuery(selectSQL);
                Log.d("hej", "test3");
                while (rs.next()) {
                    Log.d("hej", "test4");
                    dateFromDB = rs.getInt("date");
                    beginsFromDB = rs.getInt("begins");
                    endsFromDB = rs.getInt("ends");
                    locationFromDB = rs.getString("location");
                    notesFromDB = rs.getString("notes");
                    activityFromDB = rs.getString("activity");
                    Log.d("hej", "test5"+dateFromDB);

                }
                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return returnDate;

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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return dbConnection;
        }






    }

