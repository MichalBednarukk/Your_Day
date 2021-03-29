package com.example.your_day.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.your_day.R;
import com.google.android.material.navigation.NavigationView;


import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public CalendarView calendarView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);
        navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                    case R.id.day:
                        Date todayDate = new Date();
                        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int month = localDate.getMonthValue();
                        int dayOfMonth = localDate.getDayOfMonth();
                        int year = localDate.getYear();
                        String sMonth;
                        String sDayOfMonth;
                        if (month < 10) {
                            sMonth = "0" + month;
                        } else {
                            sMonth = "" + month;
                        }
                        if (dayOfMonth < 10) {
                            sDayOfMonth = "0" + dayOfMonth;
                        } else {
                            sDayOfMonth = "" + dayOfMonth;
                        }
                        String date = year + "-" + sMonth + "-" + sDayOfMonth;
                        Intent intent = new Intent(MainActivity.this
                                , DayActivity.class);
                        intent.putExtra("DATE", date);
                        startActivity(intent);
                        return true;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this,
                                "My Cart", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                    default:
                        return true;
                }
            }
        });

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String sMonth;
            String sDayOfMonth;
            month += 1;
            if (month < 10) {
                sMonth = "0" + month;
            } else {
                sMonth = "" + month;
            }
            if (dayOfMonth < 10) {
                sDayOfMonth = "0" + dayOfMonth;
            } else {
                sDayOfMonth = "" + dayOfMonth;
            }
            String date = year + "-" + sMonth + "-" + sDayOfMonth;
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}
