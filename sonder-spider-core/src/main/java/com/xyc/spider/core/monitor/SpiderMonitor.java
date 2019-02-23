package com.xyc.spider.core.monitor;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;

import java.util.Set;

/**
 * @description: 监控节点中心
 * @author: xyc
 * @create: 2019-02-02 16:38
 */
public abstract class SpiderMonitor implements IMonitor {


    /**
     * 获取所有的worker
     *
     * @return
     */
    public abstract Set<Spider> getAllSpiders(SpiderType spiderType);

    /**
     * 向注册中心注册节点信息
     *
     * @param spider
     * @return
     */
    public abstract Boolean register(Spider spider) throws Exception;

}
