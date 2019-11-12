package com.st.utils;

import java.util.Comparator;

/**
 * Created by admin on 2016/10/11.
 */
public class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

            return str1.compareTo(str2);
            }
}

