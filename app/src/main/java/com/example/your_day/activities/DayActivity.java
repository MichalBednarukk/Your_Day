package com.example.your_day.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.your_day.BuildConfig;
import com.example.your_day.DataBaseHelper;
import com.example.your_day.ImageAdapter;
import com.example.your_day.R;
import com.example.your_day.models.MediaModel;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static androidx.recyclerview.widget.RecyclerView.LayoutManager;

public class DayActivity extends AppCompatActivity implements ImageAdapter.ItemClicked, Handler.Callback {
    protected static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    protected static final int MEDIA_TYPE_VIDEO = 2;
    protected static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int LOAD_IMG_REQUEST_CODE = 300;
    private static final int LOAD_VIDEO_REQUEST_CODE = 400;
    protected DayActivity ActivityContext = null;
    protected static final String MEDIA_TABLE = "MEDIA_TABLE";
    private HandlerThread thread;
    private Handler handler;
    private String date;
    private Uri fileUri;
    private RecyclerView recyclerView;
    private ImageAdapter myAdapter;
    private LayoutManager layoutManager;
    private DataBaseHelper dataBaseHelper;
    private ArrayList<MediaModel> mediaModels;
    float x1, x2, y1, y2;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        date = getIntent().getStringExtra("DATE");
        ActivityContext = this;
        dataBaseHelper = new DataBaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);

        thread = new HandlerThread("Thread");
        thread.start();
        handler = new Handler(thread.getLooper(), this);
        handler.sendEmptyMessage(0);

        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("DAY  " + date);

        navigationView = findViewById(R.id.nv);
        navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.home:
                        Intent intent = new Intent(DayActivity.this
                                , MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.day:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                    case R.id.settings:
                        Toast.makeText(DayActivity.this,
                                "My Cart",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                    default:
                        return true;
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent touchEvent) {
                switch (touchEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = touchEvent.getX();
                        y1 = touchEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = touchEvent.getX();
                        y2 = touchEvent.getY();
                        System.out.println("x1= " + x1 + " x2= " + x2 + " y1= " + y1 + " y2= " + y2);
                        if (x1 - x2 > 400) {//Go to next day
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            try {
                                Date day = format.parse(date);
                                Calendar calendar = Calendar.getInstance();
                                assert day != null;
                                calendar.setTime(day);
                                calendar.add(Calendar.DATE, +1);
                                Date yesterday = calendar.getTime();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String stringYesterday = simpleDateFormat.format(yesterday);
                                Intent intent = new Intent(DayActivity.this
                                        , DayActivity.class);
                                intent.putExtra("DATE", stringYesterday);
                                intent.putExtra("NAVIGATION", "RIGHT");
                                finish();
                                DayActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.wait);
                                startActivity(intent);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else if (x1 - x2 < -400) {//Go to day before
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            try {
                                Date day = format.parse(date);
                                Calendar calendar = Calendar.getInstance();
                                assert day != null;
                                calendar.setTime(day);
                                calendar.add(Calendar.DATE, -1);
                                Date yesterday = calendar.getTime();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String stringYesterday = simpleDateFormat.format(yesterday);
                                Intent intent = new Intent(DayActivity.this
                                        , DayActivity.class);
                                intent.putExtra("DATE", stringYesterday);
                                intent.putExtra("NAVIGATION", "LEFT");
                                finish();
                                DayActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.wait);
                                startActivity(intent);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private File getOutputMediaFile(int type) {
        mkFolder("MyApp");//create new directory is not exist

        String timeStamp = date;
        Random generator = new Random();
        int number = 10000;
        number = generator.nextInt(number);
        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MyApp" + File.separator +
                    "VID_" + timeStamp + number + ".mp4");
        } else if (type == MEDIA_TYPE_IMAGE) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MyApp" + File.separator +
                    "IMG_" + timeStamp + number + ".JPG");

        } else {
            return null;
        }

        return mediaFile;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // After camera screen this code will excuted

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                MediaModel mediaModel = new MediaModel(1, date, fileUri.toString(), false);
                dataBaseHelper.addOne(mediaModel, MEDIA_TABLE);//add new media to the dataBase
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video add successfully", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
                Toast.makeText(this, "User cancelled the video capture.",
                        Toast.LENGTH_LONG).show();
            } else {
                // Video capture failed, advise user
                Toast.makeText(this, "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                MediaModel mediaModel = new MediaModel(1, date, fileUri.toString(), true);
                dataBaseHelper.addOne(mediaModel, MEDIA_TABLE);//add new media to the dataBase
                Toast.makeText(this, "Image add successfully"
                        , Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {

                // User cancelled the image capture
                Toast.makeText(this, "User cancelled the Image capture.",
                        Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Image capture failed.",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == LOAD_IMG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                MediaModel mediaModel = new MediaModel(1, date, getFullPathFromContentUri(DayActivity.this, selectedImage), true);
                dataBaseHelper.addOne(mediaModel, MEDIA_TABLE);//add new media to the dataBase
                Toast.makeText(this, "Image import successfully"
                        , Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {

                // User cancelled the image capture
                Toast.makeText(this, "User cancelled the Image import.",
                        Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Image import fail.",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == LOAD_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedVideo = data.getData();
                MediaModel mediaModel = new MediaModel(1, date, getFullPathFromContentUri(DayActivity.this, selectedVideo), false);
                dataBaseHelper.addOne(mediaModel, MEDIA_TABLE);//add new media to the dataBase
                Toast.makeText(this, "Video import successfully"
                        , Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {

                // User cancelled the image capture
                Toast.makeText(this, "User cancelled the Video import.",
                        Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Video import fail",
                        Toast.LENGTH_LONG).show();
            }
        }

        handler.sendEmptyMessage(0);//uploading a display to a thread
    }

    public void mkFolder(String folderName) { // make a folder under Environment.DIRECTORY_DCIM
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("myAppName", "Error: external storage is unavailable");
            return;
        }
        Log.d("myAppName", "External storage is not read only or unavailable");

        if (ContextCompat.checkSelfPermission(this, // request permission when it is not granted.
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("myAppName", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), folderName);
        int result = 0;
        if (folder.exists()) {
            Log.d("myAppName", "folder exist:" + folder.toString());
            result = 2; // folder exist
        } else {
            try {
                if (folder.mkdir()) {
                    Log.d("myAppName", "folder created:" + folder.toString());
                    result = 1; // folder created
                } else {
                    Log.d("myAppName", "creat folder fails:" + folder.toString());
                    result = 0; // creat folder fails
                }
            } catch (Exception ecp) {
                ecp.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    @Override
    public void OnItemClicked(int index, String event) {

        if (event.equals("VIEW")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (!mediaModels.get(index).getisImage()) {
                intent.setDataAndType(Uri.parse(mediaModels.get(index).getFileUri()), "video/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.parse(mediaModels.get(index).getFileUri()), "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(intent);
        }
        if (event.equals("DELETE")) {
            dataBaseHelper.deleteOneById(mediaModels.get(index).getId());
            getApplicationContext().getContentResolver().delete(Uri.parse(mediaModels.get(index).getFileUri()), null, null);
            Toast.makeText(this, "Delete successfully", Toast.LENGTH_LONG).show();
            mediaModels = dataBaseHelper.getMediaList(date);
            myAdapter = new ImageAdapter(ActivityContext, mediaModels);
            recyclerView.setAdapter(myAdapter);
        }
        if (event.equals("ADD_MEDIA")) {
            showMenu(findViewById(R.id.imageView), "camera");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this
                , MainActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }


    private void showMenu(View v,String from) {
        PopupMenu popup = new PopupMenu(DayActivity.this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.media_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(from.equals("gallery")) {
                    if (item.toString().equals("IMAGE")) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, LOAD_IMG_REQUEST_CODE);
                        return true;
                    }
                    if (item.toString().equals("VIDEO")) {
                        Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
                        videoPickerIntent.setType("video/*");
                        startActivityForResult(videoPickerIntent, LOAD_VIDEO_REQUEST_CODE);
                        return true;
                    }
                }
                if(from.equals("camera")){
                    if (item.toString().equals("IMAGE")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        // create a file to save the video
                        fileUri = FileProvider.getUriForFile(DayActivity.this, DayActivity.this.getApplicationContext().getPackageName() + ".provider", Objects.requireNonNull(getOutputMediaFile(MEDIA_TYPE_IMAGE)));

                        // set the image file name
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the Image Capture Intent
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        return true;
                    }
                    if (item.toString().equals("VIDEO")) {

                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                        // create a file to save the video
                        fileUri = FileProvider.getUriForFile(DayActivity.this, BuildConfig.APPLICATION_ID + ".provider", Objects.requireNonNull(getOutputMediaFile(MEDIA_TYPE_VIDEO)));
                        // set the image file name
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // set the video image quality to high
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        // start the Video Capture Intent
                        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                        return true;
                    }
            }
                return false;
            }
        });
        popup.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getFullPathFromContentUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                Cursor cursor = null;
                final String column = "_data";
                final String[] projection = {
                        column
                };

                try {
                    cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                            null);
                    if (cursor != null && cursor.moveToFirst()) {
                        final int column_index = cursor.getColumnIndexOrThrow(column);
                        return cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
                return null;
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.day_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(DayActivity.this, NoteActivity.class);
                intent.putExtra("DATE", date);
                startActivity(intent);
                return true;
            case R.id.importPhoto:
                showMenu(findViewById(R.id.importPhoto),"gallery");
                return true;
            default:
                if(drawerToggle.onOptionsItemSelected(item))
                    return true;
                return super.onOptionsItemSelected(item);
        }
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {column};
        try {
            try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        mediaModels = new ArrayList<>();
        mediaModels = dataBaseHelper.getMediaList(date);
        Uri path = Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/" + R.drawable.addbutton);
        mediaModels.add(new MediaModel(0,date,path.toString(),true));
        runOnUiThread(() -> {
            recyclerView.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(ActivityContext, 2);
            recyclerView.setLayoutManager(layoutManager);
            myAdapter = new ImageAdapter(ActivityContext, mediaModels);
            recyclerView.setAdapter(myAdapter);
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.quit();
    }

}

