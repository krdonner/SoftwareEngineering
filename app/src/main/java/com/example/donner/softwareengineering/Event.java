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

            String selectDate = "SELECT notes, begins, ends FROM calendar WHERE userName = " + "'" + u + "'" + " and date = '" + d + "'";
            /* String select1 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '01' and ends = '02'";
            String select2 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '02' and ends = '03'";
            String select3 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '03' and ends = '04'";
            String select4 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '04' and ends = '05'";
            String select5 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '05' and ends = '06'";
            String select6 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '06' and ends = '07'";
            String select7 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '07' and ends = '08'";
            String select8 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '08' and ends = '09'";
            String select9 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '09' and ends = '10'";
            String select10 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '10' and ends = '11'";
            String select11 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '11' and ends = '12'";
            String select12 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '12' and ends = '13'";
            String select13 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '13' and ends = '14'";
            String select14 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '14' and ends = '15'";
            String select15 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '15' and ends = '16'";
            String select16 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '16' and ends = '17'";
            String select17 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '17' and ends = '18'";
            String select18 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '18' and ends = '19'";
            String select19 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '19' and ends = '20'";
            String select20 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '20' and ends = '21'";
            String select21 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '21' and ends = '22'";
            String select22 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '22' and ends = '23'";
            String select23 = "SELECT notes FROM calender WHERE userName = " + "'" + user + "'" + " and date = " + date + " and begins = '23' and ends = '00'"; */

            String note, begin, end;

            try {
                dbConnection = getDBConnection();
                Statement st = dbConnection.createStatement();
                ResultSet rs = st.executeQuery(selectDate);

                while (rs.next()) {
                    note = rs.getString("notes");
                    begin = rs.getString("begins");
                    end = rs.getString("ends");
                    System.out.println(note);
                    System.out.println(begin);
                    System.out.println(end);

                    final int finalBegin = Integer.parseInt(begin);
                    final int finalEnd = Integer.parseInt(end);
                    final String finalNote = note;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalBegin == 0){
                                zeroOne.setText(finalNote);
                            }
                            if (finalBegin <= 1 && finalEnd >= 1){
                                oneTwo.setText(finalNote);
                            }
                            if (finalBegin <= 2 && finalEnd >= 2){
                                twoThree.setText(finalNote);
                            }
                            if (finalBegin <= 3 && finalEnd >= 3){
                                threeFour.setText(finalNote);
                            }
                            if (finalBegin <= 4 && finalEnd >= 4){
                                fourFive.setText(finalNote);
                            }
                            if (finalBegin <= 5 && finalEnd >= 5){
                                fiveSix.setText(finalNote);
                            }
                            if (finalBegin <= 6 && finalEnd >= 6){
                                sixSeven.setText(finalNote);
                            }
                            if (finalBegin <= 7 && finalEnd >= 7){
                                sevenEight.setText(finalNote);
                            }
                            if (finalBegin <= 8 && finalEnd >= 8){
                                eightNine.setText(finalNote);
                            }
                            if (finalBegin <= 9 && finalEnd >= 9){
                                nineTen.setText(finalNote);
                            }
                            if (finalBegin <= 10 && finalEnd >= 10){
                                tenEleven.setText(finalNote);
                            }
                            if (finalBegin <= 11 && finalEnd >= 11){
                                elevenTwelve.setText(finalNote);
                            }
                            if (finalBegin <= 12 && finalEnd >= 12){
                                twelveThirteen.setText(finalNote);
                            }
                            if (finalBegin <= 13 && finalEnd >= 13){
                                thirteenFourteen.setText(finalNote);
                            }
                            if (finalBegin <= 14 && finalEnd >= 14){
                                fourteenFifteen.setText(finalNote);
                            }
                            if (finalBegin <= 15 && finalEnd >= 15){
                                fifteenSixteen.setText(finalNote);
                            }
                            if (finalBegin <= 16 && finalEnd >= 16){
                                sixteenSeventeen.setText(finalNote);
                            }
                            if (finalBegin <= 17 && finalEnd >= 17){
                                seventeenEighteen.setText(finalNote);
                            }
                            if (finalBegin <= 18 && finalEnd >= 18){
                                eighteenNineteen.setText(finalNote);
                            }
                            if (finalBegin <= 19 && finalEnd >= 19){
                                nineteenTwenty.setText(finalNote);
                            }
                            if (finalBegin <= 20 && finalEnd >= 20){
                                twentyTwentyone.setText(finalNote);
                            }
                            if (finalBegin <= 21 && finalEnd >= 21){
                                twentyoneTwentytwo.setText(finalNote);
                            }
                            if (finalBegin <= 22 && finalEnd >= 22){
                                twentytwoTwentythree.setText(finalNote);
                            }
                            if (finalBegin <= 23 && finalEnd >= 23){
                                twentythreeZero.setText(finalNote);
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