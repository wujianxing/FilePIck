package com.example.mylibrary.CompositeFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by wujianxing on 16/12/23.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */

public class PatternFilter implements FileFilter, Serializable {

    private Pattern mPattern;
    private boolean mDirectoriesFilter;

    public PatternFilter(Pattern pattern, boolean directoriesFilter) {
        mPattern = pattern;
        mDirectoriesFilter = directoriesFilter;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() && !mDirectoriesFilter || mPattern.matcher(f.getName()).matches();
    }
}
