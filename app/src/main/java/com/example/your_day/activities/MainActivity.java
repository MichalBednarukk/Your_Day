package com.example.your_day.activities;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.your_day.R;
import com.google.android.material.navigation.NavigationView;


import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public CalendarView calendarView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @SuppressLint({"NonConstantResourceId", "RtlHardcoded"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.bringToFront();

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
    protected boolean checkPermission(String type) {
        if(type.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
        if (ContextCompat.checkSelfPermission(this, type) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
        }
        if(type.equals(Manifest.permission.CAMERA)){
            if (ContextCompat.checkSelfPermission(this, type) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return false;
    }

    protected void requestPermission(String type) {
    if(type.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, type)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{type}, 100);
            }
        }}
        if(type.equals(Manifest.permission.CAMERA)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, type)) {
                Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{type}, 100);
                }
            }}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Log.e("value", "Permission Denied, You cannot use camera .");
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

}
