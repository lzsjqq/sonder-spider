package com.xyc.spider.core.monitor;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;

import java.util.Set;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-02 19:03
 */
public class RedisSpiderMonitor  extends  SpiderMonitor{

    @Override
    public Set<Spider> getAllSpiders(SpiderType spiderType) {
        return null;
    }

    @Override
    public Boolean register(Spider spider) throws Exception {
        return null;
    }
}
