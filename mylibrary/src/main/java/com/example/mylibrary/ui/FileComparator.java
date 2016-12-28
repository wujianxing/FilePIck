package com.example.mylibrary.ui;

import java.io.File;
import java.util.Comparator;

/**
 * Created by wujianxing on 16/12/26.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */
public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        if (o1 == o2 ){
            return 0;
        }
        if (o1.isDirectory() && o2.isFile()){
            return -1;
        }
        if (o1.isFile() && o2.isDirectory()){
            return 1;
        }
        return o1.getName().compareToIgnoreCase(o2.getName());
    }


}
