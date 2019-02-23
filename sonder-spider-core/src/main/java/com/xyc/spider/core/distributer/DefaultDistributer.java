package com.xyc.spider.core.distributer;

import com.xyc.spider.core.base.ISonderSpiderConfig;
import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;
import com.xyc.spider.core.bean.Task;
import com.xyc.spider.core.monitor.SpiderMonitor;
import com.xyc.spider.core.monitor.ZookeeperSpiderMonitor;
import com.xyc.spider.core.task.ITaskRepertory;
import com.xyc.spider.core.task.RedisTaskRepertory;

import java.util.Set;

/**
 * @description: 使用zookeeper做监控，redis做存储，采用默认分发策略选择Spider
 * @author: xyc
 * @create: 2019-02-02 18:55
 * @see ZookeeperSpiderMonitor
 * @see DefaultDistributeStrategy
 * @see RedisTaskRepertory
 * @see
 */
public class DefaultDistributer extends AbstractDistributer {

    /**
     * @param spiderMonitor
     * @param distributeStrategy
     * @param taskRepertory
     */
    public DefaultDistributer(SpiderMonitor spiderMonitor, IDistributeStrategy distributeStrategy, ITaskRepertory taskRepertory) {
        super(spiderMonitor, distributeStrategy, taskRepertory);
    }

    public DefaultDistributer(ISonderSpiderConfig config, int numberOfReplicas) {
        super(ZookeeperSpiderMonitor.getInstance(config), new DefaultDistributeStrategy(numberOfReplicas), new RedisTaskRepertory());
    }
    @Override
    public Spider chooseSpider(Set<Spider> spiders, Task task) {
        return this.distributeStrategy.chooseSpider(spiders, task);
    }
@Override
    public Set<Spider> getAllSpiders(SpiderType type) {
        return this.spiderMonitor.getAllSpiders(type);
    }

    public Boolean addTask(Spider spider, Task task) {
        return this.taskRepertory.addTask(spider, task);
    }

}
