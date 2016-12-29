package com.example.mylibrary.ui;

import android.app.Activity;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;

import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylibrary.CompositeFilter.CompositeFilter;
import com.example.mylibrary.R;
import com.example.mylibrary.widget.EmptyRecyclerView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujianxing on 16/12/23.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */
public class DirectoryFragment extends Fragment {
    private String TAG="DirectoryFragment";

    List<File>result = new ArrayList<>();

    interface  FileClickListener{
        void onFileClicked(File clickedFile);
    }
    private static final String ARG_FILE_PATH = "arg_file_path";
    private static final String ARG_FILTER = "arg_filter";
    public static final String ARG_SELECT_TYPE = "arg_secect_type";

    private View mEmptyView;
    private String mPath;
    private String mType="1";

    private CompositeFilter mFilter;

    private EmptyRecyclerView mDirectoryRecyclerView;
    private DirectoryAdapter mDirectoryAdapter;
    private FileClickListener mFileClickListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFileClickListener = (FileClickListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileClickListener =null;
    }
    public static DirectoryFragment getInstance(String path,CompositeFilter filter,String type){
        DirectoryFragment instance = new DirectoryFragment();

        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, path);
        args.putString(ARG_SELECT_TYPE, type);
        args.putSerializable(ARG_FILTER, filter);

        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory,container,false);
        mDirectoryRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.directory_recycler_view);
        mEmptyView =view.findViewById(R.id.directory_empty_view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArgs();
        initFilesList();
    }

    private Handler handler = new Handler() {
        // 该方法运行在主线程中
        // 接收到handler发送的消息，对UI进行操作
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0x123) {
                if (mDirectoryAdapter!=null) {
                    Log.e(TAG,"nofity........");
                    mDirectoryAdapter.notifyDataSetChanged();
                }
            }
            if (msg.what == 0x124) {
                ( (AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
            }
        }
    };
    public void hh(final String filePath1){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetAllFiles(new File(filePath1));

                }
        }).start();

    }
    public void GetAllFiles(File filePath){
        Log.e(TAG,"filepath :"+filePath);
        int i;
        File[] files = filePath.listFiles();
        if (files!=null) {
            for (i = 0; i < files.length; i++) {
//                try {
//                    Thread.sleep(0);//睡眠6秒 模拟执行耗时任务
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                dosome(files[i]);



                if (files[i].isDirectory()) {
                    GetAllFiles(files[i]);
                } else if (files[i].getName().toLowerCase().endsWith(".apk")) {
                    result.add(files[i]);
                    handler.sendEmptyMessage(0x123);//发送消息

                }
            }
        }

    }


    private Toolbar mToolbar;

    private void dosome(File file) {

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);


        //Show back button
        if (((AppCompatActivity) getActivity()).getSupportActionBar()!=null){
            ((AppCompatActivity) getActivity()). getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            ((AppCompatActivity) getActivity()).getSupportActionBar()
        }
        //Truncate start of toolbar title


        try {

            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);

            TextView textView = (TextView) f.get(mToolbar);
            textView.setEllipsize(TextUtils.TruncateAt.START);
        }catch (Exception ingored){

        }
        updateTitle(file.toString());

    }
    String mTitle;
    private void updateTitle(String title) {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() !=null){
            mTitle =title;
            handler.sendEmptyMessage(0x124);//发送消息

        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.menu_activity_main, menu);
    }


    private void initFilesList() {
        mToolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_activity_main);

        if (mType.equals("2")){
            Log.e(TAG,"type :"+mType);
//            result= GetAllFiles(new File(mPath)) ;
            hh(mPath);

        }else {
            result = FileUtils.getFileListByDirPath(mPath, mFilter);

            Log.e(TAG,"nonono");
        }

        mDirectoryAdapter = new DirectoryAdapter(getActivity(),result);

        mDirectoryAdapter.setmOnITemClickListener(new DirectoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mFileClickListener !=null){
                    mFileClickListener.onFileClicked(mDirectoryAdapter.getModel(position));
                    //得到地址
                }
            }
        });
        mDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDirectoryRecyclerView.setAdapter(mDirectoryAdapter);
        mDirectoryRecyclerView.setEmptyView(mEmptyView);
    }




    /*
    unchecked
     */
    private void initArgs() {
        if (getArguments().getString(ARG_FILE_PATH) !=null){
            mPath = getArguments().getString(ARG_FILE_PATH);
        }
        if (getArguments().getString(ARG_SELECT_TYPE) !=null){
            mType = getArguments().getString(ARG_SELECT_TYPE);
            Log.e(TAG,"myType:  "+ mType);
        }
        mFilter =(CompositeFilter)getArguments().getSerializable(ARG_FILTER);
    }

}
