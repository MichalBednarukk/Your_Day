package com.example.your_day.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.your_day.DataBaseHelper;
import com.example.your_day.R;
import com.example.your_day.models.NoteModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_TABLE = "NOTE_TABLE";
    private TextInputLayout textInputLayout;
    private TextInputEditText textInput;
    private Button btnAddNote;
    private DataBaseHelper dataBaseHelper;
    private String date;
    private  NoteModel noteModelTest;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        date = getIntent().getStringExtra("DATE");
        textInput = findViewById(R.id.textInput);
        btnAddNote = findViewById(R.id.btnAddNote);
        textInputLayout = findViewById(R.id.textInputLayout);
        dataBaseHelper = new DataBaseHelper(this);
        noteModelTest = dataBaseHelper.getNoteByDate(date);
        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Note of " + date);
        navigationView = findViewById(R.id.nv);
        navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.home:
                        intent = new Intent(NoteActivity.this
                                , MainActivity.class);
                        startActivity(intent);
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
                        intent = new Intent(NoteActivity.this
                                , DayActivity.class);
                        intent.putExtra("DATE", date);
                        startActivity(intent);
                        return true;
                    case R.id.settings:
                        Toast.makeText(NoteActivity.this,
                                "My Cart", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                    default:
                        return true;
                }
            }
        });
        if (noteModelTest.getId() != 0) {
            textInput.setText(noteModelTest.getFileUri());
        }

        btnAddNote.setOnClickListener(v -> {
            boolean status;
            NoteModel noteModel = new NoteModel(1, date, textInputLayout.getEditText().getText().toString());
            if (noteModelTest.getId() == 0) {
                status = dataBaseHelper.addOne(noteModel, NOTE_TABLE);//add new media to the dataBase

            } else {
                status = dataBaseHelper.updateNoteByDate(date, noteModel, noteModelTest.getId());
            }
            if (status) {
                Toast.makeText(NoteActivity.this, "Text add successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NoteActivity.this, "Error, try again", Toast.LENGTH_LONG).show();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}