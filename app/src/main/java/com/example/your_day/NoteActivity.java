package com.example.your_day;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.your_day.models.NoteModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_TABLE = "NOTE_TABLE";
    TextInputLayout textInputLayout;
    TextInputEditText textInput;
    Button btnAddNote;
    DataBaseHelper dataBaseHelper;
    String date;
    NoteModel noteModelTest;

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

        if(noteModelTest.getId() != 0){
            textInput.setText(noteModelTest.getFileUri());
        }

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status;
                NoteModel noteModel = new NoteModel(1, date, textInputLayout.getEditText().getText().toString());
                if(noteModelTest.getId() == 0){
                 status  = dataBaseHelper.addOne(noteModel, NOTE_TABLE);//add new media to the dataBase

            }
                else {
                 status = dataBaseHelper.updateNoteByDate(date,noteModel,noteModelTest.getId());
                }
            if(status){
                Toast.makeText(NoteActivity.this, "Text add successfully", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(NoteActivity.this, "Error, try again", Toast.LENGTH_LONG).show();
            }
            }
        });
    }
}