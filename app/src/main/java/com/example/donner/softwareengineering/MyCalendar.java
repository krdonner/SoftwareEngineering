package com.example.donner.softwareengineering;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCalendar extends Activity implements OnClickListener {
    private static final String tag = "MyCalendarActivity";

    private TextView currentMonth;
    private ImageView previousMonth, nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    private static final String dateTemplate = "MMMM yyyy";
    private static String yearToAddToEvent, monthToAddToEvent, dayToAddToEvent, currentMonthName;
    public static String date;
    public static int dateInInteger;
    public static ArrayList<Integer> dates;
    public String user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        final Intent intent = getIntent();
        user = intent.getStringExtra("user");

        dates = (ArrayList<Integer>) getIntent().getSerializableExtra("dates");

        _calendar = Calendar.getInstance(Locale.UK);
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);


        previousMonth = (ImageView) this.findViewById(R.id.prevMonth);
        previousMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == previousMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            setGridCellAdapterToDate(month, year);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Mån", "Tis",
                "Ons", "Tor", "Fre", "Lör", "Sön"};
        private final String[] months = {"Januari", "Februari", "Mars",
                "April", "Maj", "Juni", "Juli", "Augusti", "September",
                "Oktober", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");

        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            printMonth(month, year);

            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        private void printMonth(int mm, int yy) {
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int lastMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

            if (currentMonth == 11) {
                lastMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(lastMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                lastMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(lastMonth);
                nextMonth = 1;
            } else {
                lastMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(lastMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 2;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(lastMonth)
                        + "-"
                        + prevYear);
            }

            for (int i = 1; i <= daysInMonth; i++) {
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            for (int i = 0; i < list.size() % 7; i++) {
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            String[] day_color = list.get(position).split("-");
            final String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row
                            .findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }
            gridcell.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    yearToAddToEvent = Integer.toString(year);
                    if (currentMonthName.equals("Januari")) {
                        monthToAddToEvent = "1";
                    } else if (currentMonthName.equals("Februari")) {
                        monthToAddToEvent = "2";
                    } else if (currentMonthName.equals("Mars")) {
                        monthToAddToEvent = "3";
                    } else if (currentMonthName.equals("April")) {
                        monthToAddToEvent = "4";
                    } else if (currentMonthName.equals("Maj")) {
                        monthToAddToEvent = "5";
                    } else if (currentMonthName.equals("Juni")) {
                        monthToAddToEvent = "6";
                    } else if (currentMonthName.equals("Juli")) {
                        monthToAddToEvent = "7";
                    } else if (currentMonthName.equals("Augusti")) {
                        monthToAddToEvent = "8";
                    } else if (currentMonthName.equals("September")) {
                        monthToAddToEvent = "9";
                    } else if (currentMonthName.equals("Oktober")) {
                        monthToAddToEvent = "10";
                    } else if (currentMonthName.equals("November")) {
                        monthToAddToEvent = "11";
                    } else if (currentMonthName.equals("December")) {
                        monthToAddToEvent = "12";
                    }
                    dayToAddToEvent = theday;

                    date = yearToAddToEvent + monthToAddToEvent + dayToAddToEvent;

                    Intent intent = new Intent(MyCalendar.this, DayOverview.class);
                    intent.putExtra("user", user);
                    intent.putExtra("date", date);
                    intent.putExtra("theDay", theday);
                    startActivity(intent);
                    return false;
                }
            });

            gridcell.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    long dayInInteger = getItemId(position);
                    yearToAddToEvent = Integer.toString(year);
                    if (currentMonthName.equals("Januari")) {
                        monthToAddToEvent = "1";
                    } else if (currentMonthName.equals("Februari")) {
                        monthToAddToEvent = "2";
                    } else if (currentMonthName.equals("Mars")) {
                        monthToAddToEvent = "3";
                    } else if (currentMonthName.equals("April")) {
                        monthToAddToEvent = "4";
                    } else if (currentMonthName.equals("Maj")) {
                        monthToAddToEvent = "5";
                    } else if (currentMonthName.equals("Juni")) {
                        monthToAddToEvent = "6";
                    } else if (currentMonthName.equals("Juli")) {
                        monthToAddToEvent = "7";
                    } else if (currentMonthName.equals("Augusti")) {
                        monthToAddToEvent = "8";
                    } else if (currentMonthName.equals("September")) {
                        monthToAddToEvent = "9";
                    } else if (currentMonthName.equals("Oktober")) {
                        monthToAddToEvent = "10";
                    } else if (currentMonthName.equals("November")) {
                        monthToAddToEvent = "11";
                    } else if (currentMonthName.equals("December")) {
                        monthToAddToEvent = "12";
                    }
                    dayToAddToEvent = theday;

                    date = yearToAddToEvent + monthToAddToEvent + dayToAddToEvent;
                    getCurrentDayOfMonth();
                    Toast.makeText(getApplicationContext(), yearToAddToEvent + monthToAddToEvent + dayToAddToEvent, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MyCalendar.this, AddEvent.class);
                    intent.putExtra("date", date);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });
            if (currentMonthName.equals("Januari")) {
                monthToAddToEvent = "1";
            } else if (currentMonthName.equals("Februari")) {
                monthToAddToEvent = "2";
            } else if (currentMonthName.equals("Mars")) {
                monthToAddToEvent = "3";
            } else if (currentMonthName.equals("April")) {
                monthToAddToEvent = "4";
            } else if (currentMonthName.equals("Maj")) {
                monthToAddToEvent = "5";
            } else if (currentMonthName.equals("Juni")) {
                monthToAddToEvent = "6";
            } else if (currentMonthName.equals("Juli")) {
                monthToAddToEvent = "7";
            } else if (currentMonthName.equals("Augusti")) {
                monthToAddToEvent = "8";
            } else if (currentMonthName.equals("September")) {
                monthToAddToEvent = "9";
            } else if (currentMonthName.equals("Oktober")) {
                monthToAddToEvent = "10";
            } else if (currentMonthName.equals("November")) {
                monthToAddToEvent = "11";
            } else if (currentMonthName.equals("December")) {
                monthToAddToEvent = "12";
            }
            dayToAddToEvent = theday;

            dateInInteger = Integer.parseInt(year + monthToAddToEvent + dayToAddToEvent);
            gridcell.setText(theday);
            gridcell.setTag(dateInInteger);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources()
                        .getColor(R.color.lightgray));
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(
                        R.color.dark));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.orrange));
            }
            for (int i = 0; i < dates.size(); i++) {
                if (dateInInteger == dates.get(i) && !day_color[1].equals("GREY")) {
                    gridcell.setTextColor(getResources().getColor(R.color.sky));
                }
            }
            return row;
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
            } catch (ParseException e) {
                System.out.println(e);
            }
        }
    }
}