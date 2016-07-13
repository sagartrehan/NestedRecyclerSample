package com.ecommerce.util;

import java.util.List;

public class AppUtil {

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isValid(String string) {
        return !isEmpty(string);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isValid(List<T> list) {
        return !isEmpty(list);
    }

}
