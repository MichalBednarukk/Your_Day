package com.example.your_day.models;

public class NoteModel {
    private int id;
    private String date;
    private String fileUri;

    public NoteModel(){

    }

    public NoteModel(int id, String date, String fileUri) {
        this.id = id;
        this.date = date;
        this.fileUri = fileUri;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", fileUri='" + fileUri + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }
}
