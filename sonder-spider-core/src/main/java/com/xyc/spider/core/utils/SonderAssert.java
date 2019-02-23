package com.xyc.spider.core.utils;

import com.xyc.spider.core.datasource.JedisDao;
import com.xyc.spider.core.exception.ExceptionCode;
import com.xyc.spider.core.exception.SonderSpiderException;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 15:14
 */
public class SonderAssert {

    public static void arrIsEmpty(String[] nodeNames) {
        if (nodeNames == null || nodeNames.length <= 0) {
            throw new SonderSpiderException(ExceptionCode.IS_BLANK);
        }
    }

    public static void isBlank(String val) {
        if (StringUtils.isBlank(val)) {
            throw new SonderSpiderException(ExceptionCode.IS_BLANK);
        }
    }

    public static void isNull(Object data) {
        if (null == data) {
            throw new SonderSpiderException(ExceptionCode.IS_BLANK);
        }
    }
}
