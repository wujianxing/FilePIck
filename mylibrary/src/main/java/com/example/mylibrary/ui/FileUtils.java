package com.example.mylibrary.ui;

import android.graphics.Path;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.R.attr.key;
import static android.R.attr.path;
import static android.support.design.R.id.ifRoom;
import static android.support.design.R.id.info;
import static android.support.design.R.id.search_badge;

/**
 * Created by wujianxing on 16/12/26.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */
public class FileUtils {
    private static AbstractCollection<File> arrayList;
    private static String TAG = "FileUtils";


    public static List<File> getFileListByDirPath(String path, FileFilter filter) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);

        if (files == null) {
            return new ArrayList<>();
        }
        List<File> result = Arrays.asList(files);
        Collections.sort(result, new FileComparator());
        return result;
    }

    public static String cutLastSegementOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSie(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups) + " " + units[digitGroups]);
    }

    /**
     * 自己添加的
     */

}
