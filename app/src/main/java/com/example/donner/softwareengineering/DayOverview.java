package com.example.donner.softwareengineering;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DayOverview extends Activity {

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";

    ListView listView;
    String date, theDay;
    int dateInteger;
    static int theDayInteger, dateFromDB, beginsFromDB, endsFromDB, idFromDB;
    static String notesFromDB, locationFromDB, activityFromDB;
    String[] values;
    public String user;

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

            if (dateInteger == dateFromDB) {
                values[beginsFromDB] += "\n" + "Activity: " + activityFromDB + " \n" + "Location: " + locationFromDB + "\n"
                        + "Notes: " + notesFromDB + "\n" + "Ends: " + endsFromDB;
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

                                    Intent mStartActivity = new Intent(DayOverview.this, Main.class);
                                    int mPendingIntentId = 123456;
                                    PendingIntent mPendingIntent = PendingIntent.getActivity(DayOverview.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager mgr = (AlarmManager) DayOverview.this.getSystemService(DayOverview.this.ALARM_SERVICE);
                                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                    System.exit(0);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }

    private int getDate() throws SQLException {
        Connection dbConnection;
        int returnDate = 0;
        int returnBegins = 0;

        String selectSQL = "SELECT id, activity, date, begins, notes, ends, location FROM calendar WHERE username = " + "'" + user + "'" + " and date = " + dateInteger;
        try {
            dbConnection = getDBConnection();
            Statement st = dbConnection.createStatement();

            ResultSet rs = st.executeQuery(selectSQL);
            while (rs.next()) {
                dateFromDB = rs.getInt("date");
                beginsFromDB = rs.getInt("begins");
                endsFromDB = rs.getInt("ends");
                locationFromDB = rs.getString("location");
                notesFromDB = rs.getString("notes");
                activityFromDB = rs.getString("activity");
                idFromDB = rs.getInt("id");
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
            try {
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
                Class.forName(DB_DRIVER);
            } catch (ClassNotFoundException e) {
                System.out.println(e);
            }

            try {
                dbConnection = DriverManager.getConnection(
                        DB_CONNECTION, DB_USER, DB_PASSWORD);
                return dbConnection;
            } catch (SQLException e) {
                System.out.println(e);
            }
            return dbConnection;
        }
    }
}