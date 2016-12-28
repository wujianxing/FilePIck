package com.example.mylibrary.CompositeFilter;

import java.io.File;

/**
 * Created by wujianxing on 16/12/23.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */
public class HiddenFilter implements java.io.FileFilter {
    @Override
    public boolean accept(File f) {
        return !f.isHidden();
    }
}
