package com.example.mark.filepicker;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mylibrary.MaterialFilePicker;
import com.example.mylibrary.ui.FilePickerActivity;
import com.google.android.gms.plus.PlusOneButton;

import java.util.regex.Pattern;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 * Use the {@link DemoFragment#} factory method to
 * create an instance of this fragment.
 */
public class DemoFragment extends Fragment implements View.OnClickListener {

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;


    public DemoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);

        Button pickButton = (Button) view.findViewById(R.id.pick_from_fragment);
        pickButton.setOnClickListener(this);
        Button pickButton_sousuo = (Button) view.findViewById(R.id.pick_from_fragment_card);
        pickButton_sousuo.setOnClickListener(this);


        return view;
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(getContext(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withSupportFragment(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false)             // Set directories filterable (false by default)
                .withHiddenFiles(true)                    // Show hidden files and folders
                .withRootPath(Environment.getRootDirectory().getPath())
                .start();
    }
    private void openFilePicker2() {
        new MaterialFilePicker()
                .withSelectType("2")
                .withSupportFragment(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false)             // Set directories filterable (false by default)
                .withHiddenFiles(true)                    // Show hidden files and folders
//                .withRootPath(Environment.getExternalStorageState())
                .withRootPath(Environment.getRootDirectory().getPath())
                .start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            if (path != null) {
                Log.d("Path (fragment): ", path);
                Toast.makeText(getContext(), "Picked file in fragment: " + path, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        String permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            switch (v.getId()){
                case R.id.pick_from_fragment_card:
                    openFilePicker2();
                    break;
                case R.id.pick_from_fragment:
                    openFilePicker();
                    break;
            }
        }

    }
}
