package com.example.mylibrary.CompositeFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wujianxing on 16/12/23.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */

public class CompositeFilter implements FileFilter,Serializable {
    private ArrayList<FileFilter>mFilters;
    public CompositeFilter (ArrayList<FileFilter> filters){
        mFilters = filters;
    }

    @Override
    public boolean accept(File pathname) {
        for (FileFilter filter : mFilters) {
            if (!filter.accept(pathname)) {
                return false;
            }
        }
        return true;
    }
}
