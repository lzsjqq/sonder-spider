package com.xyc.spider.core.distributer;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.Task;

import java.util.Set;

/**
 * @description: 任务分配策略
 * @author: xyc
 * @create: 2019-02-02 16:52
 */
public interface IDistributeStrategy {
    /**
     * 选择一个spider跑任务
     *
     * @param task
     * @return
     */
    public Spider chooseSpider(Set<Spider> spiders, Task task);
}
