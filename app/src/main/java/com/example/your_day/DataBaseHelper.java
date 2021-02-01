package com.example.your_day;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.your_day.models.MediaModel;
import com.example.your_day.models.NoteModel;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String MEDIA_TABLE = "MEDIA_TABLE";
    public static final String NOTE_TABLE = "NOTE_TABLE";
    public static final String COLUMN_MEDIA_DATE = "MEDIA_DATE";
    public static final String COLUMN_NOTE_DATE = "TEXT_DATE";
    public static final String COLUMN_MEDIA_URI = "MEDIA_URI";
    public static final String COLUMN_NOTE_URI = "TEXT_URI";
    public static final String COLUMN_MEDIA_IS_IMAGE = "MEDIA_IsIMAGE";
    public static final String COLUMN_ID = "ID";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "Database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE " + MEDIA_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MEDIA_DATE + " TEXT, " + COLUMN_MEDIA_URI + " TEXT, " + COLUMN_MEDIA_IS_IMAGE + " BOOL)";
        String table2 = "CREATE TABLE " + NOTE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE_DATE + " TEXT, " + COLUMN_NOTE_URI + " TEXT)";
        db.execSQL(table1);
        db.execSQL(table2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Object object, String dataBaseName) {


        if (dataBaseName.equals(MEDIA_TABLE)) {
            MediaModel mediaModel = (MediaModel) object;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MEDIA_DATE, mediaModel.getDate());
            contentValues.put(COLUMN_MEDIA_URI, mediaModel.getFileUri());
            contentValues.put(COLUMN_MEDIA_IS_IMAGE, mediaModel.getisImage());
            long insert = db.insert(MEDIA_TABLE, null, contentValues);
            if (insert == -1) {
                return false;
            } else {
                return true;
            }
        }
        if (dataBaseName.equals(NOTE_TABLE)) {
            NoteModel noteModel = (NoteModel) object;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NOTE_DATE, noteModel.getDate());
            contentValues.put(COLUMN_NOTE_URI, noteModel.getFileUri());
            long insert = db.insert(NOTE_TABLE, null, contentValues);
            if (insert == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public ArrayList<MediaModel> getMediaList(String date) {
        ArrayList<MediaModel> mediaModelslist = new ArrayList<>();
        String queryString = "SELECT * FROM " + MEDIA_TABLE + " WHERE " + COLUMN_MEDIA_DATE + " LIKE " + "'%" + date + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int mediaID = cursor.getInt(0);
                String mediaDate = cursor.getString(1);
                String mediaUri = cursor.getString(2);
                boolean mediaIsImage = cursor.getInt(3) == 1 ? true : false;

                MediaModel newMediaModel = new MediaModel(mediaID, mediaDate, mediaUri, mediaIsImage);
                mediaModelslist.add(newMediaModel);
            } while (cursor.moveToNext());

        } else {

        }
        cursor.close();
        db.close();
        return mediaModelslist;
    }
    public NoteModel getNoteByDate(String date){
        NoteModel noteMode = new NoteModel(0,"0","0");
        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + COLUMN_NOTE_DATE + " LIKE " + "'%" + date + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                noteMode.setId(cursor.getInt(0));
                noteMode.setDate(cursor.getString(1));
                noteMode.setFileUri(cursor.getString(2));
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return noteMode;
        }
        else {
            return noteMode;
        }
    }
    public boolean updateNoteByDate(String date,NoteModel noteModel, int id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTE_DATE, noteModel.getDate());
        contentValues.put(COLUMN_NOTE_URI, noteModel.getFileUri());
        int val= db.update(NOTE_TABLE, contentValues, COLUMN_ID + "=" + id, null);

        if (val == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean deleteOneById (int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM MEDIA_TABLE WHERE ID = " + id);
        return true;
    }
}
