package com.example.your_day.models;

public class MediaModel {
    private int id;
    private String date;
    private String fileUri;
    private boolean isImage;

    public MediaModel(int id, String date, String fileUri, boolean isImage) {
        this.id = id;
        this.date = date;
        this.fileUri = fileUri;
        this.isImage = isImage;
    }

    public MediaModel() {
    }

    @Override
    public String toString() {
        return "MediaModel{" +
                "id=" + id +
                ", date=" + date +
                ", fileUri=" + fileUri +
                ", isImage=" + isImage +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean getisImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
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
