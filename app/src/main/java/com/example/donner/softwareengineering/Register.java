package com.example.donner.softwareengineering;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Register extends Activity {
    EditText username, password, passwordRepeat;
    Button registerUser;

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        // getActionBar().hide();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordRepeat = (EditText) findViewById(R.id.passwordRepeat);
        registerUser = (Button) findViewById(R.id.registerButton);

    }

    public void registerClicked(View v) {
        if (password.getText().toString().equals(passwordRepeat.getText().toString())) {
            RegisterAsync r = new RegisterAsync(username.getText().toString());
            r.execute(getApplicationContext());
        } else {
            Toast.makeText(getApplicationContext(), "Passwords does not match.", Toast.LENGTH_SHORT).show();
            password.getText().clear();
            passwordRepeat.getText().clear();
        }
    }

    private class RegisterAsync extends AsyncTask<Object, Object, Cursor> {

        String user;

        private RegisterAsync(String usr) {
            user = usr;
        }

        @Override
        protected Cursor doInBackground(Object... params) {

            try {
                String un = doesUsernameExist();
                handleUserRegistration(un);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void handleUserRegistration(String uname) {

            String u = uname;

            System.out.println("user = " + user);
            System.out.println("u = " + u);

            if (user.equals(u)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        username.getText().clear();
                        password.getText().clear();
                        passwordRepeat.getText().clear();
                        Toast.makeText(getApplicationContext(), "User with that name already exist.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (!user.equals(u)) {
                try {
                    insertRecordIntoTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        private String doesUsernameExist() throws SQLException {

            Connection dbConnection;
            String returnValue = null;

            String selectSQL = "SELECT username FROM user WHERE username = " + "'" + username.getText().toString() + "'";

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

        private void insertRecordIntoTable() throws SQLException {

            Connection dbConnection = null;
            PreparedStatement preparedStatement = null;

            String insertTableSQL = "INSERT INTO user"
                    + "(Username, password) VALUES"
                    + "(?,?)";

            try {
                dbConnection = getDBConnection();
                preparedStatement = dbConnection.prepareStatement(insertTableSQL);

                preparedStatement.setString(1, username.getText().toString());
                preparedStatement.setString(2, password.getText().toString());

                preparedStatement.executeUpdate();

                System.out.println("Record is inserted into USER table!");

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            } finally {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (dbConnection != null) {
                    dbConnection.close();
                }
            }
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