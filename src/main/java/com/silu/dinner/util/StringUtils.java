package com.silu.dinner.util;

/**
 * Created by silu on 15-2-8.
 */
public class StringUtils {
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
