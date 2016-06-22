package com.kms.cura.utils;

import com.kms.cura.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/17/2016.
 */
public class DataUtils {
    public static List<String> getListName(List<? extends Entity> list){
        List<String> names = new ArrayList<>();
        for (int i = 0; i< list.size(); ++i){
            names.add(list.get(i).getName());
        }
        return names;
    }
}
