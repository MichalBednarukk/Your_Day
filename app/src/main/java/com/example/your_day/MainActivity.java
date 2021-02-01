package com.example.your_day;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.CalendarView;

import androidx.annotation.NonNull;



public class MainActivity extends Activity
{
    public CalendarView calendarView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String sMonth;
                String sDayOfMonth;
                month += 1;
                if(month<10){ sMonth = "0"+ month;}
                else{ sMonth = "" +month;}
                if(dayOfMonth<10){ sDayOfMonth = "0"+dayOfMonth;}
                else{ sDayOfMonth = ""+dayOfMonth;}
                String  date = year + "-" + sMonth + "-" + sDayOfMonth;
                Intent intent = new Intent(MainActivity.this
                        , DayActivity.class);
                intent.putExtra("DATE", date);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();

    }

}
