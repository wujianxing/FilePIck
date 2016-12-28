package com.example.mylibrary.ui;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by wujianxing on 16/12/26.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */

public class FileFilterTxT implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        String filename = pathname.getName().toLowerCase();
        if(filename.contains(".txt")){
            return false;
        }else{
            return true;
        }
    }
}