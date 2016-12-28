package com.example.mylibrary.ui;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.mylibrary.CompositeFilter.CompositeFilter;
import com.example.mylibrary.CompositeFilter.PatternFilter;
import com.example.mylibrary.R;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FilePickerActivity extends AppCompatActivity  implements DirectoryFragment.FileClickListener{

    public static final String ARG_START_PATH = "arg_start_path";
    public static final String ARG_CURRENT_PATH = "arg_current_path";

    public static final String ARG_FILTER = "arg_filter";
    public static final String ARG_SELECT_TYPE = "arg_secect_type";

    public static final String STATE_START_PATH = "state_start_path";
    private static final String STATE_CURRENT_PATH = "state_current_path";

    public static final String RESULT_FILE_PATH = "result_file_path";
    private static final int HANDLE_CLICK_DELAY = 150;


    private Toolbar mToolbar;
    private String mStartPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String mCurrentPath = mStartPath;

    private CompositeFilter mFilter;
    private String mSelectType ;
    private String TAG="FilePickerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        /**
         * 初始化参数
         */
        initArguments();
        /* 初始化View*/
        initViews();
        /**
         * 返回
         */
        initToolbar();

        if (savedInstanceState != null){
            mStartPath = savedInstanceState.getString(STATE_START_PATH);
            mCurrentPath = savedInstanceState.getString(STATE_CURRENT_PATH);
        }else {
            initFragment();
        }
    }
    /**
     * 初始化参数
     */
    private void initArguments() {
        if (getIntent().hasExtra(ARG_SELECT_TYPE)){
            Log.e(TAG,"filePicker "+getIntent().hasExtra(ARG_SELECT_TYPE));
            mSelectType=getIntent().getStringExtra(ARG_SELECT_TYPE);
        }
        if (getIntent().hasExtra(ARG_FILTER)){
            Serializable filter = getIntent().getSerializableExtra(ARG_FILTER);

            if (filter instanceof Pattern){
                ArrayList<FileFilter>filters = new ArrayList<>();
                filters.add(new PatternFilter((Pattern)filter,false ));
                mFilter = new CompositeFilter(filters);
            }else {
                mFilter = (CompositeFilter)filter;
            }
        }
        if (getIntent().hasExtra(ARG_START_PATH)){
            mStartPath = getIntent().getStringExtra(ARG_START_PATH);
            mCurrentPath = mStartPath;
        }
        if (getIntent().hasExtra(ARG_CURRENT_PATH)){
            String currentPath = getIntent().getStringExtra(ARG_CURRENT_PATH);
            if (currentPath.startsWith(mStartPath)){
                mCurrentPath = currentPath;
            }
        }
    }
    //初始化fragment
    private void initFragment() {
//        if (getIntent().getStringExtra(ARG_SELECT_TYPE)!=null){
//
//        }else {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, DirectoryFragment.getInstance(
                            mStartPath, mFilter,mSelectType))
                    .commit();
//        }
    }


    private void initToolbar() {
        setSupportActionBar(mToolbar);;
        //Show back button
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        //Truncate start of toolbar title


        try {

            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);

            TextView textView = (TextView) f.get(mToolbar);
            textView.setEllipsize(TextUtils.TruncateAt.START);
        }catch (Exception ingored){

        }
        updateTitle();
    }

    private void updateTitle() {
        if (getSupportActionBar() !=null){
            String title = mCurrentPath.isEmpty() ? "/":mCurrentPath;
            if (title.startsWith(mStartPath)){
                title=title.replaceFirst(mStartPath,"Device");
            }
            getSupportActionBar().setTitle(title);
        }
    }

    private void initViews() {
    mToolbar =(Toolbar)findViewById(R.id.toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CURRENT_PATH,mCurrentPath);
        outState.putString(STATE_START_PATH,mStartPath);
    }

    @Override
    public void onFileClicked(final File clickedFile) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleFileClicked(clickedFile);
            }
        },HANDLE_CLICK_DELAY);
    }

    private void handleFileClicked(File clickedFile) {
        if (clickedFile.isDirectory()){
            addFragmentToBackStack(clickedFile.getPath());
            mCurrentPath = clickedFile.getPath();
            updateTitle();
        }else {
            setResultAndFinish(clickedFile.getPath());
        }
    }

    private void setResultAndFinish(String filePath) {
        Intent data = new Intent();
        data.putExtra(RESULT_FILE_PATH,filePath);
        setResult(RESULT_OK,data);
        finish();
    }

    private void addFragmentToBackStack(String path) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container,DirectoryFragment.getInstance(path,mFilter,mSelectType))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
            mCurrentPath =FileUtils.cutLastSegementOfPath(mCurrentPath);
            updateTitle();
        }else {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }
}
