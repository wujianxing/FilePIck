package com.example.mylibrary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;

import com.example.mylibrary.CompositeFilter.CompositeFilter;
import com.example.mylibrary.CompositeFilter.HiddenFilter;
import com.example.mylibrary.CompositeFilter.PatternFilter;
import com.example.mylibrary.ui.FilePickerActivity;
import com.example.mylibrary.ui.FileUtils;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by wujianxing on 16/12/23.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */

public class MaterialFilePicker {
    private Activity mActivity;
    private Fragment mFragment;    //app fragment;
    private android.support.v4.app.Fragment mSupportFragment;

    private Integer mRequestCode;
    private Pattern mFileFilter;
    private Boolean mDirectoriesFilter = false;
    private String mRootPath;
    private String mCurrentPath;
    private Boolean mShowHidden = false;

    private String mSelectType;
    private String TAG="MaterialFilePicker";


    /**
     * Specifies activity ,which will be used to start file picker
     * @param activity
     * @return
     */
    public MaterialFilePicker withActivity(Activity activity){
        if (mSupportFragment != null || mFragment != null){
            throw new RuntimeException("you must pass either Activity,Fragment or SupportFragment");
        }
        mActivity = activity;
        return this;
    }
    /**
     * Specifies fragment, which will be used to start picker
     */
    public MaterialFilePicker withFragment(Fragment fragment){
        if (mSupportFragment != null || mActivity != null){
            throw new RuntimeException("You must pass either Activity ,Fragment or SupportFragment");
        }
        mFragment = fragment;
        return this;
    }

    /**
     * Specifies support fragment which will beused to start picker
     * @param fragment
     * @return
     */
    public MaterialFilePicker withSupportFragment (android.support.v4.app.Fragment fragment){

        if (mActivity !=null || mFragment !=null){
            throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
        }
        mSupportFragment = fragment;
        return this;
    }

    /**
     * Specifies request code that used in activity result
     * @param requestCode
     * @return
     */
    public MaterialFilePicker withRequestCode(int requestCode){
        mRequestCode =requestCode;
        return this;
    }
    /**
     * Hides files that matched by specified regular expression.
     */
    public MaterialFilePicker withFilter(Pattern pattern){
        mFileFilter =pattern;
        return this;
    }
    /**
     * If directoriesFiler is true directories will also be affected by filter,the default value of directories filter is false
     */
    public MaterialFilePicker withFilterDirectories(boolean directoriesFilter) {
        mDirectoriesFilter = directoriesFilter;
        return this;
    }

    /**
     * Specifies root directory for picker,user can't go upper that specified path
     * @param rootPath
     * @return
     */
    public MaterialFilePicker withRootPath(String rootPath){
        mRootPath = rootPath;
        return this;
    }
    /**
     * Specifies start directory for picker,which will be shown to user at the beginning
     */
    public MaterialFilePicker withPath(String path){
        mCurrentPath =path;
        return this;
    }

    /**
     * Show or hide hidden files in packer
     * @param show
     * @return
     */
    public MaterialFilePicker withHiddenFiles(boolean show){
        mShowHidden = show;
        return this;
    }

    public MaterialFilePicker withSelectType(String SelectType){
        mSelectType =SelectType;
        return this;
    }
    /**
     *
     */
    public CompositeFilter getFilter(){
        ArrayList<FileFilter>filters = new ArrayList<>();

        //是否隐藏
        if (!mShowHidden){
            filters.add(new HiddenFilter());
        }
        if (mFileFilter !=null){
            filters.add(new PatternFilter(mFileFilter,mDirectoriesFilter));
        }
        return new CompositeFilter(filters);
    }

    public Intent getIntent(){
        CompositeFilter filter = getFilter();

        Activity activity = null;
        if (mActivity != null){
            activity = mActivity;
        }else if (mFragment != null){
            activity=mFragment.getActivity();
        }else if (mSupportFragment !=null){
            activity = mSupportFragment.getActivity();
        }

        //讲过滤条件装进去
        Intent intent = new Intent(activity, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.ARG_FILTER,filter);

        if (mRootPath !=null){
            intent.putExtra(FilePickerActivity.ARG_START_PATH,mRootPath);
        }
        if (mCurrentPath != null){
            intent.putExtra(FilePickerActivity.ARG_CURRENT_PATH,mCurrentPath);
        }
        if (mSelectType != null){
            intent.putExtra(FilePickerActivity.ARG_SELECT_TYPE,mSelectType);
            Log.e(TAG,"mSelectype :"+mSelectType);

        }
        return intent;
    }
    public void start(){
        if (mActivity == null && mFragment == null && mSupportFragment == null){
            throw   new RuntimeException("You must pass Activity/Fragment by calling withActiity/withFragment/withSupportFragment method");
        }
        if (mRequestCode == null){
            throw new RuntimeException("You must pass request code by calling withRequestCode method");
        }
        Intent intent = getIntent();

        if (mActivity != null){
            mActivity.startActivityForResult(intent,mRequestCode);
        }else if (mFragment !=null){
            mFragment.startActivityForResult(intent,mRequestCode);
        }else {
            mSupportFragment.startActivityForResult(intent,mRequestCode);
        }
    }








}
