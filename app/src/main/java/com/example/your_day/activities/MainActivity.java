package com.example.your_day.activities;

import android.content.Intent;
import android.os.Bundle;

import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.your_day.R;


public class MainActivity extends AppCompatActivity
{
    public CalendarView calendarView;
Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbarMainActivity);
        setSupportActionBar(toolbar);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
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
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

}
