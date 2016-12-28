package com.example.mylibrary.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylibrary.CompositeFilter.CompositeFilter;
import com.example.mylibrary.R;
import com.example.mylibrary.widget.EmptyRecyclerView;

import java.io.File;
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArgs();
        initFilesList();
    }

    private void initFilesList() {
        List<File>result = null;
        if (mType.equals("2")){
            Log.e(TAG,"type :"+mType);
            result= GetAllFiles(new File(mPath)) ;
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
    List<File> txtList = new ArrayList<>(); ;

    public List<File> GetAllFiles(File filePath){
        Log.e(TAG,"filepath :"+filePath);
        int i;
        File[] files = filePath.listFiles();
        if (files!=null) {
            for (i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    GetAllFiles(files[i]);
                } else if (files[i].getName().toLowerCase().endsWith(".apk")) {
                    txtList.add(files[i]);
                }
            }
        }
        return txtList;
    }
}
