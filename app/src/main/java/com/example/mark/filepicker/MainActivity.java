package com.example.mark.filepicker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.mylibrary.MaterialFilePicker;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_CODE =0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pickButton =(Button)findViewById(R.id.pick_from_activity);
        Button pickButtonWithFilter =(Button)findViewById(R.id.pick_from_activity_withFilter);
        pickButtonWithFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker("2");
            }
        });
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker("1");
            }
        });

    }
    private void checkPermissionsAndOpenFilePicker(String type) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            switch (type) {
                case "1":
                    openFilePicker();
                    break;
                case "2":
//                    openFilePickerWithFilter();
            }

        }
    }
    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false)             // Set directories filterable (false by default)
                .withHiddenFiles(true)                    // Show hidden files and folders
                .withRootPath(Environment.getRootDirectory().getPath())
                .start();
    }
}
