package com.example.donner.softwareengineering;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Event extends Activity {

    final String DB_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_CONNECTION = "jdbc:mysql://89.160.102.7:3306/projekt";
    final String DB_USER = "ruut";
    final String DB_PASSWORD = "rooot";

    private TextView zeroOne, oneTwo, twoThree, threeFour, fourFive, fiveSix, sixSeven, sevenEight, eightNine, nineTen, tenEleven, elevenTwelve, twelveThirteen, thirteenFourteen, fourteenFifteen, fifteenSixteen, sixteenSeventeen, seventeenEighteen, eighteenNineteen, nineteenTwenty, twentyTwentyone, twentyoneTwentytwo, twentytwoTwentythree, twentythreeZero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent(); // gets the previously created intent
        String user = intent.getStringExtra("username"); // will return "FirstKeyValue"
        System.out.println(user);
        String day = intent.getStringExtra("date"); // will return "SecondKeyValue"
        System.out.println(day);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        // getActionBar().hide();

        fetchDayInformation(user, day);

        zeroOne = (TextView) findViewById(R.id.zeroOneText);
        oneTwo = (TextView) findViewById(R.id.oneTwoText);
        twoThree = (TextView) findViewById(R.id.twoThreeText);
        threeFour = (TextView) findViewById(R.id.threeFourText);
        fourFive = (TextView) findViewById(R.id.fourFiveText);
        fiveSix = (TextView) findViewById(R.id.fiveSixText);
        sixSeven = (TextView) findViewById(R.id.sixSevenText);
        sevenEight = (TextView) findViewById(R.id.sevenEightText);
        eightNine = (TextView) findViewById(R.id.eightNineText);
        nineTen = (TextView) findViewById(R.id.nineTenText);
        tenEleven = (TextView) findViewById(R.id.tenElevenText);
        elevenTwelve = (TextView) findViewById(R.id.elevenTwelveText);
        twelveThirteen = (TextView) findViewById(R.id.twelveThirteenText);
        thirteenFourteen = (TextView) findViewById(R.id.thirteenFourteenText);
        fourteenFifteen = (TextView) findViewById(R.id.fourteenFifteenText);
        fifteenSixteen = (TextView) findViewById(R.id.fifteenSixteenText);
        sixteenSeventeen = (TextView) findViewById(R.id.sixteenSeventeenText);
        seventeenEighteen = (TextView) findViewById(R.id.seventeenEighteenText);
        eighteenNineteen = (TextView) findViewById(R.id.eighteenNineteenText);
        nineteenTwenty = (TextView) findViewById(R.id.nineteenTwentyText);
        twentyTwentyone = (TextView) findViewById(R.id.twentyTwentyoneText);
        twentyoneTwentytwo = (TextView) findViewById(R.id.twentyoneTwentytwoText);
        twentytwoTwentythree = (TextView) findViewById(R.id.twentytwoTwentythreeText);
        twentythreeZero = (TextView) findViewById(R.id.twentythreeZeroText);
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

    public void fetchDayInformation(String user, String date){
        DayInformationAsync dia = new DayInformationAsync(user, date);
        dia.execute(getApplicationContext());
    }

    private class DayInformationAsync extends AsyncTask<Object, Object, Cursor> {

        String user;
        String day;

        private DayInformationAsync(String usr, String date) {
            user = usr;
            day = date;
        }

        @Override
        protected Cursor doInBackground(Object... params) {
            getDayInformationFromDatabase(user, day);
            return null;
        }

        private void getDayInformationFromDatabase(String user, String date){
            String u = user;
            String d = date;

            Connection dbConnection;

            String selectDate = "SELECT activity, begins, ends FROM calendar WHERE userName = " + "'" + u + "'" + " and date = '" + d + "'";

            String activity, begin, end;

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();
                ResultSet rs = st.executeQuery(selectDate);

                while (rs.next()) {
                    activity = rs.getString("activity");
                    begin = rs.getString("begins");
                    end = rs.getString("ends");
                    System.out.println(activity);
                    System.out.println(begin);
                    System.out.println(end);

                    final int finalBegin = Integer.parseInt(begin);
                    final int finalEnd = Integer.parseInt(end);
                    final String finalActivity = activity;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalBegin == 0){
                                zeroOne.setText(finalActivity);
                            }
                            if (finalEnd < 1){
                                oneTwo.setText("");
                            } else if (finalBegin <= 1 && finalEnd >= 1){
                                oneTwo.setText(finalActivity);
                            }
                            if (finalBegin <= 2 && finalEnd >= 2){
                                twoThree.setText(finalActivity);
                            }
                            if (finalBegin <= 3 && finalEnd >= 3){
                                threeFour.setText(finalActivity);
                            }
                            if (finalBegin <= 4 && finalEnd >= 4){
                                fourFive.setText(finalActivity);
                            }
                            if (finalBegin <= 5 && finalEnd >= 5){
                                fiveSix.setText(finalActivity);
                            }
                            if (finalBegin <= 6 && finalEnd >= 6){
                                sixSeven.setText(finalActivity);
                            }
                            if (finalBegin <= 7 && finalEnd >= 7){
                                sevenEight.setText(finalActivity);
                            }
                            if (finalBegin <= 8 && finalEnd >= 8){
                                eightNine.setText(finalActivity);
                            }
                            if (finalBegin <= 9 && finalEnd >= 9){
                                nineTen.setText(finalActivity);
                            }
                            if (finalBegin <= 10 && finalEnd >= 10){
                                tenEleven.setText(finalActivity);
                            }
                            if (finalBegin <= 11 && finalEnd >= 11){
                                elevenTwelve.setText(finalActivity);
                            }
                            if (finalBegin <= 12 && finalEnd >= 12){
                                twelveThirteen.setText(finalActivity);
                            }
                            if (finalBegin <= 13 && finalEnd >= 13){
                                thirteenFourteen.setText(finalActivity);
                            }
                            if (finalBegin <= 14 && finalEnd >= 14){
                                fourteenFifteen.setText(finalActivity);
                            }
                            if (finalBegin <= 15 && finalEnd >= 15){
                                fifteenSixteen.setText(finalActivity);
                            }
                            if (finalBegin <= 16 && finalEnd >= 16){
                                sixteenSeventeen.setText(finalActivity);
                            }
                            if (finalBegin <= 17 && finalEnd >= 17){
                                seventeenEighteen.setText(finalActivity);
                            }
                            if (finalBegin <= 18 && finalEnd >= 18){
                                eighteenNineteen.setText(finalActivity);
                            }
                            if (finalBegin <= 19 && finalEnd >= 19){
                                nineteenTwenty.setText(finalActivity);
                            }
                            if (finalBegin <= 20 && finalEnd >= 20){
                                twentyTwentyone.setText(finalActivity);
                            }
                            if (finalBegin <= 21 && finalEnd >= 21){
                                twentyoneTwentytwo.setText(finalActivity);
                            }
                            if (finalBegin <= 22 && finalEnd >= 22){
                                twentytwoTwentythree.setText(finalActivity);
                            }
                            if (finalBegin <= 23 && finalEnd >= 23){
                                twentythreeZero.setText(finalActivity);
                            }
                        }
                    });
                }
                rs.close();
                st.close();
                dbConnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
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