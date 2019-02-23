package com.xyc.spider.core.distributer;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;
import com.xyc.spider.core.bean.Task;

import java.util.Set;

/**
 * @description:使用redis作为任务的分配中心
 * @author: xyc
 * @create: 2019-02-02 16:26
 */
public class RedisDistributer extends AbstractDistributer {


    @Override
    public Spider chooseSpider(Set<Spider> spiders, Task task) {
        return null;
    }

    @Override
    public Set<Spider> getAllSpiders(SpiderType type) {
        return null;
    }

    @Override
    public Boolean addTask(Spider spider, Task task) {
        return null;
    }
}
