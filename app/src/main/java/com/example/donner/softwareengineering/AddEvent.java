package com.example.donner.softwareengineering;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddEvent extends Activity {

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";

    public static String date;
    Button button;
    EditText edActivity, edNotes;
    int starts, ends;
    private static String act, note;
    private Statement state;
    public String user;
    String location;
    EditText edLocation, edStarts, edEnds;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);

        Bundle myBundle = getIntent().getExtras();
        if (myBundle != null) {
            user = myBundle.getString("user");
            date = myBundle.getString("date");
        }

        button = (Button)findViewById(R.id.saveButton);
        edActivity = (EditText)findViewById(R.id.activity);
        edNotes = (EditText)findViewById(R.id.notes);
        edStarts = (EditText)findViewById(R.id.textStart);
        edEnds = (EditText)findViewById(R.id.textEnd);
        edLocation = (EditText)findViewById(R.id.location);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                starts = Integer.parseInt(edStarts.getText().toString());
                ends = Integer.parseInt(edEnds.getText().toString());
                act = edActivity.getText().toString();
                note = edNotes.getText().toString();
                location = edLocation.getText().toString();

                AddAsync aa = new AddAsync();
                aa.execute(getApplicationContext());
            }
        });
    }

    private class AddAsync extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... params) {
            storeInDB();
            return null;
        }

        private String storeInDB() {

            Connection dbConnection;
            String returnValue = null;
            int id = 0;
            id = getEventID();

            String insertTableSQL = "INSERT INTO calendar"
                    + "(id, username, activity, notes, begins, ends, location, date) VALUES"
                    + "(" + id +"," + "'"+user+"'"+","+ "'"+act+"'"+","+ "'"+note+"'"+","+starts+","+ends+"," +"'"+location+"'"+","+date+")";

            try {
                dbConnection = getDBConnection();
                setStatement(dbConnection.createStatement());
                getStatement().executeUpdate(insertTableSQL);

            } catch (SQLException e) {
                System.out.println(e);
            }
            return returnValue;
        }

        private int getEventID() {

            Connection dbConnection;
            int returnValue = 0;

            String idSQL = "SELECT id FROM calendar ORDER BY id";

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();

                ResultSet rs = st.executeQuery(idSQL);

                while (rs.next()) {
                    returnValue = rs.getInt("id");
                }
                rs.close();
                st.close();
                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
            return returnValue+1;
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

    public Statement getStatement() {
        return this.state;
    }

    public void setStatement(Statement state) {
        this.state = state;
    }

}