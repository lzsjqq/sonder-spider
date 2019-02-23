package com.xyc.spider.core.base;

import com.xyc.spider.core.exception.ExceptionCode;
import com.xyc.spider.core.exception.SonderSpiderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 10:36
 */
public class SpiderConfig implements ISonderSpiderConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpiderConfig.class);
    private static Properties prop = new Properties();
    private static SpiderConfig spiderConfig;

    private SpiderConfig() {
        super();
        InputStream inputStream = SpiderConfig.class.getClassLoader().getResourceAsStream("spider-core.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new SonderSpiderException(ExceptionCode.ERROR_FOR_CONFIG, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new SonderSpiderException(ExceptionCode.ERROR_FOR_CONFIG, e);
            }
        }
    }

    public static SpiderConfig getInstance() {
        if (spiderConfig == null) {
            synchronized (SpiderConfig.class) {
                if (spiderConfig == null) {
                    spiderConfig = new SpiderConfig();
                }
            }
        }
        return spiderConfig;
    }

    /**
     * @param key
     * @return
     */
    @Override
    public String getString(String key) {
        return prop.getProperty(key);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Integer getInt(String key) {
        String value = prop.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    @Override
    public long getLong(String key, int df) {
        String value = prop.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return Long.valueOf(value);
        }
        return df;
    }

    @Override
    public Integer getInt(String key, Integer df) {
        String value = prop.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        }
        return df;
    }
}
