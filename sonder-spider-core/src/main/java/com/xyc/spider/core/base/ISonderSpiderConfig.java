package com.xyc.spider.core.base;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 14:52
 */
public interface ISonderSpiderConfig {
    /**
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * @param key
     * @param df  默认值
     * @return
     */
    Integer getInt(String key, Integer df);

    /**
     * @param key
     * @return
     */
    Integer getInt(String key);

    long getLong(String key, int i);
}
