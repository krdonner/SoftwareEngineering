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

public class EditEvent extends Activity {

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";

    Button button;
    EditText edActivity, edNotes;
    int starts, ends;
    private static String act, note;
    private Statement state;
    public String user, location;
    public int beginning;
    public static int id;
    private EditText edStarts, edEnds, edLocation;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent);

        Bundle myBundle = getIntent().getExtras();
        if (myBundle != null) {
            user = myBundle.getString("user");
            beginning = myBundle.getInt("begins");
        }
        button = (Button) findViewById(R.id.saveButton);
        edActivity = (EditText) findViewById(R.id.activity);
        edNotes = (EditText) findViewById(R.id.notes);
        edStarts = (EditText) findViewById(R.id.textStart);
        edEnds = (EditText) findViewById(R.id.textEnd);
        edLocation = (EditText) findViewById(R.id.location);

        setupAsync setA = new setupAsync();
        setA.execute(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starts = Integer.parseInt(edStarts.getText().toString());
                ends = Integer.parseInt(edEnds.getText().toString());
                act = edActivity.getText().toString();
                note = edNotes.getText().toString();
                location = edLocation.getText().toString();
                EditAsync ea = new EditAsync();
                ea.execute(getApplicationContext());
            }
        });
    }

    private class setupAsync extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... params) {
            id = setupValues();
            System.out.println("ID = " + id);
            return null;
        }

        private int setupValues() {

            int returnValue = 0;
            Connection dbConnection;

            int identification;
            int begins, ends;
            String activity, notes, location;

            String idSQL = "SELECT id, activity, notes, begins, ends, location FROM calendar WHERE userName = " + "'" + user + "'" + " and begins = " + "'" + beginning + "'";

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();

                ResultSet rs = st.executeQuery(idSQL);

                while (rs.next()) {
                    identification = rs.getInt("id");
                    returnValue = identification;
                    activity = rs.getString("activity");
                    notes = rs.getString("notes");
                    begins = rs.getInt("begins");
                    ends = rs.getInt("ends");
                    location = rs.getString("location");

                    final String finalActivity = activity;
                    final String finalNotes = notes;
                    final int finalStarts = begins;
                    final int finalEnds = ends;
                    final String finalLocation = location;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edActivity.setText(finalActivity);
                            edNotes.setText(finalNotes);
                            edLocation.setText(finalLocation);
                            edStarts.setText(String.valueOf(finalStarts));
                            edEnds.setText(String.valueOf(finalEnds));
                        }
                    });
                }
                rs.close();
                st.close();
                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
            System.out.println("returnValue = " + returnValue);
            return returnValue;
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

    private class EditAsync extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... params) {

            updateDB();
            return null;
        }

        private void updateDB() {

            Connection dbConnection;

            System.out.println("updateDB ID = " + id);

            String updateTableSQL = "UPDATE calendar "
                    + "SET activity = " + "'" + act + "'" + "," + " notes = " + "'" + note + "'" + "," + " begins = " + starts + "," + "ends = " + ends + "," + " location = " + "'" + location + "'"
                    + " WHERE id = " + id;

            try {
                dbConnection = getDBConnection();
                setStatement(dbConnection.createStatement());
                getStatement().executeUpdate(updateTableSQL);
            } catch (SQLException e) {
                System.out.println(e);
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

    public Statement getStatement() {
        return this.state;
    }

    public void setStatement(Statement state) {
        this.state = state;
    }

}