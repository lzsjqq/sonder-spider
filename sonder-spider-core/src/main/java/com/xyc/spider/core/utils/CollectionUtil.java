package com.xyc.spider.core.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 23:19
 */
public class CollectionUtil {

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotNullOrEmpty(Collection collection) {
        return !isNullOrEmpty(collection);
    }

    public static boolean isNotNullOrEmpty(Map map) {
        return !isNullOrEmpty(map);
    }


}
