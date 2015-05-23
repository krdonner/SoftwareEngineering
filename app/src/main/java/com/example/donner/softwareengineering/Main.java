package com.example.donner.softwareengineering;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Main extends Activity {

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";
    public ArrayList<Integer>dates = new ArrayList<Integer>();

    private static EditText username;
    public static EditText password;

    public static EditText getUsername() {
        return username;
    }

    public static void setUsername(EditText username) {
        Main.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // getActionBar().hide();

        setUsername((EditText) findViewById(R.id.username));
        password = (EditText) findViewById(R.id.password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerButton(View view) {
        startActivity(new Intent(this, Register.class));
    }

    public void loginButton(View view) {
        if (getUsername().getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Login details missing.", Toast.LENGTH_SHORT).show();
            getUsername().getText().clear();
            password.getText().clear();
        } else {
            LoginAsync l = new LoginAsync(getUsername().getText().toString(), password.getText().toString());
            l.execute(getApplicationContext());
        }
    }

    private class LoginAsync extends AsyncTask<Object, Object, Cursor> {

        String user;
        String pass;

        private LoginAsync(String usr, String pw) {
            user = usr;
            pass = pw;
        }

        @Override
        protected Cursor doInBackground(Object... params) {

            try {
                getDates();
                String un = doesUsernameExist();
                System.out.println("un = " + un);
                String pw = getRelatedPassword();
                System.out.println("pw = " + pw);
                handleLogin(un, pw);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void handleLogin(String uname, String passw) {

            String u = uname;
            String p = passw;

            if (user.equals(u) && pass.equals(p)) {
                System.out.println("Logging in!");
                Intent intent = new Intent(Main.this, MyCalendarActivity.class);
                intent.putExtra("dates", dates);

                startActivity(intent);

            } else if (!user.equals(u) || !pass.equals(p)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getUsername().getText().clear();
                        password.getText().clear();
                        Toast.makeText(getApplicationContext(), "Wrong login information.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private String doesUsernameExist() throws SQLException {

            Connection dbConnection;
            String returnValue = null;

            String selectSQL = "SELECT username FROM user WHERE username = " + "'" + getUsername().getText().toString() + "'";

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();

                ResultSet rs = st.executeQuery(selectSQL);

                while (rs.next()) {
                    returnValue = rs.getString("Username");
                    System.out.println(returnValue);
                }
                rs.close();
                st.close();
                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return returnValue;
        }
        private String getDates() throws SQLException {

            Connection dbConnection;
            String returnValue = null;

            String selectSQL = "SELECT date FROM calendar WHERE username = " + "'" + getUsername().getText().toString() + "'";

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();

                ResultSet rs = st.executeQuery(selectSQL);

                while (rs.next()) {
                         dates.add(rs.getInt("date"));

                }


                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return returnValue;
        }


        private String getRelatedPassword() throws SQLException {

            Connection dbConnection;
            String returnValue = null;

            String selectSQL = "SELECT password FROM user WHERE username = " + "'" + getUsername().getText().toString() + "'";

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();

                ResultSet rs = st.executeQuery(selectSQL);

                while (rs.next()) {
                    returnValue = rs.getString("password");
                    System.out.println(returnValue);
                }
                rs.close();
                st.close();
                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return returnValue;
        }

        private Connection getDBConnection() {

            Connection dbConnection = null;

            try {
                Class.forName(DB_DRIVER);
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            try {
                dbConnection = DriverManager.getConnection(
                        DB_CONNECTION, DB_USER, DB_PASSWORD);
                return dbConnection;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return dbConnection;
        }
    }
}