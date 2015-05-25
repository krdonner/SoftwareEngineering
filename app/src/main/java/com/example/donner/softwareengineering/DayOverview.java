package com.example.donner.softwareengineering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    static int idFromDB;
    String[] values;
    private Statement state;
    static ResultSet rs;
    public String user;
    private Statement st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dayoverview);
        Bundle myBundle = getIntent().getExtras();
        if (myBundle != null) {
            date = myBundle.getString("date");
            theDay = myBundle.getString("theDay");
            user = myBundle.getString("user");
        }
        dateInteger = Integer.parseInt(date);
        theDayInteger = Integer.parseInt(theDay);
        listView = (ListView) findViewById(R.id.listView);

        DayOverviewAsync doa = new DayOverviewAsync();
        doa.execute();

        values = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23"};
        System.out.print(dateInteger + "   " + dateFromDB);
    }


    private class DayOverviewAsync extends AsyncTask<Object, Object, Cursor> {

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
            Log.e("testar", dateInteger + " " + dateFromDB);

            if (dateInteger == dateFromDB) {
                values[beginsFromDB] += " " + activityFromDB + " " + " " + locationFromDB;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2, android.R.id.text2, values);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    final int itemPosition = position;
                    String itemValue = (String) listView.getItemAtPosition(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(DayOverview.this);
                    builder.setMessage("Edit or remove the event?")
                            .setCancelable(false)
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(DayOverview.this, EditEvent.class);
                                    intent.putExtra("user", user);
                                    intent.putExtra("begins", itemPosition);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteAsync da = new deleteAsync();
                                    da.execute();

                                    Intent intent = new Intent(DayOverview.this, MyCalendarActivity.class);
                                    startActivity(intent);

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }

            });


        }
    }


    private int getDate() throws SQLException {
        Log.d("hej", "test1");
        Connection dbConnection;
        int returnDate = 0;
        int returnBegins = 0;

        String selectSQL = "SELECT id, activity, date, begins, notes, ends, location FROM calendar WHERE username = " + "'" + user + "'" + " and date = " + dateInteger;
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
                idFromDB = rs.getInt("id");
                Log.d("hej", "test5" + dateFromDB);

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
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            dbConnection = DriverManager.getConnection(
                    "jdbc:mysql://89.160.102.7:3306/projekt" + "?user=" + "ruut"
                            + "&password=" + "rooot");
            return dbConnection;
        } catch (Exception e) {
            System.out.println(e);
        }
        return dbConnection;

    }

    public Statement getSt() {
        return this.st;
    }

    public void setSt(Statement st) {
        this.st = st;
    }



    private class deleteAsync extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... params) {

            try {
                deleteFromDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }


        private void deleteFromDB() throws SQLException {

            Log.d("Delete", "delete");

            Connection dbConnection;
            String returnValue = null;


            try {
                Log.e("ID", "id" + idFromDB);
                String deleteSQL = "DELETE from Calendar WHERE ID = ?";
                PreparedStatement preparedStatement = getDBConnection().prepareStatement(deleteSQL);
                preparedStatement.setInt(1, idFromDB);
                preparedStatement.executeUpdate();

                getDBConnection().close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }



        }



        private Connection getDBConnection() {

            Connection dbConnection = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                System.out.println(e);
            }

            try {
                dbConnection = DriverManager.getConnection(
                        "jdbc:mysql://89.160.102.7:3306/projekt" + "?user=" + "ruut"
                                + "&password=" + "rooot");
                return dbConnection;
            } catch (Exception e) {
                System.out.println(e);
            }
            return dbConnection;
        }
    }





}