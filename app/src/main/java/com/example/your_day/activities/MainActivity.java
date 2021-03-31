package com.example.your_day.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.your_day.DayAdapter;
import com.example.your_day.R;
import com.google.android.material.navigation.NavigationView;


import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DayAdapter.ItemClicked {
    public CalendarView calendarView;
    private ArrayList<Integer> dayList;
    protected MainActivity ActivityContext = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView recyclerView;
    private DayAdapter myAdapter;

    @SuppressLint({"NonConstantResourceId", "RtlHardcoded"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityContext = this;
        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        recyclerView = findViewById(R.id.recyclerViewMain);
        dayList = new ArrayList<>();
        for (int i = 0; i <= 700; i++) {
            dayList.add(i);
        }
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new DayAdapter(ActivityContext, dayList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1)) {
                    for (int i = 0; i <= 700; i++) {
                        dayList.add(i);
                    }
                    myAdapter = new DayAdapter(ActivityContext, dayList);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.scrollToPosition(dayList.size() - 700);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) { //do your work
                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MyApp");
                if (folder.exists()) {
                    Log.d("myAppName", "folder exist:" + folder.toString());
                } else {
                    try {
                        if (folder.mkdir()) {
                            Log.d("myAppName", "folder created:" + folder.toString());
                        } else {
                            Log.d("myAppName", "creat folder fails:" + folder.toString());
                        }
                    } catch (Exception ecp) {
                        ecp.printStackTrace();
                    }
                }
            } else {
                requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        navigationView.setNavigationItemSelectedListener(item -> {
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
        });
//        calendarView = findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
//            String sMonth;
//            String sDayOfMonth;
//            month += 1;
//            if (month < 10) {
//                sMonth = "0" + month;
//            } else {
//                sMonth = "" + month;
//            }
//            if (dayOfMonth < 10) {
//                sDayOfMonth = "0" + dayOfMonth;
//            } else {
//                sDayOfMonth = "" + dayOfMonth;
//            }
//            String date = year + "-" + sMonth + "-" + sDayOfMonth;
//            Intent intent = new Intent(MainActivity.this
//                    , DayActivity.class);
//            intent.putExtra("DATE", date);
//            startActivity(intent);
//        });

    }

    protected boolean checkPermission(String type) {
        if (ContextCompat.checkSelfPermission(this, type) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    protected void requestPermission(String type) {
        if (type.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, type)) {
                Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{type}, 100);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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

    @Override
    public void OnItemClicked(int index, LocalDate localDate) {
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
    }
}
