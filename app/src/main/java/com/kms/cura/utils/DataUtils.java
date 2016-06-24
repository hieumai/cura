package com.kms.cura.utils;

import com.kms.cura.entity.Entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/17/2016.
 */
public class DataUtils {
    public static List<String> getListName(List<? extends Entity> list) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            names.add(list.get(i).getName());
        }
        return names;
    }

    public static String showUnicode(String src) {
        if (src == null) {
            return null;
        }
        String convert = "";
        try {
            convert = new String(src.getBytes("ISO-8859-1"), "UTF-8");
            return convert;
        } catch (UnsupportedEncodingException e) {
            return src;
        }
    }
}