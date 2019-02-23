package com.xyc.spider.core.distributer;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;
import com.xyc.spider.core.bean.Task;
import com.xyc.spider.core.monitor.SpiderMonitor;
import com.xyc.spider.core.task.ITaskRepertory;

import java.util.Set;

/**
 * @description: 定义分配中心的基本使用方式
 * @author: xyc
 * @create: 2019-02-02 16:29
 */
public abstract class AbstractDistributer implements IDistributer {
    protected SpiderMonitor spiderMonitor;// 监控
    protected IDistributeStrategy distributeStrategy;// 分发策略
    protected ITaskRepertory taskRepertory;//仓库

    public AbstractDistributer() {
        super();
    }

    // TODO 去重策略

    /**
     * @param spiderMonitor
     * @param distributeStrategy
     */
    public AbstractDistributer(SpiderMonitor spiderMonitor, IDistributeStrategy distributeStrategy, ITaskRepertory taskRepertory) {
        super();
        this.spiderMonitor = spiderMonitor;
        this.distributeStrategy = distributeStrategy;
        this.taskRepertory = taskRepertory;
    }

    @Override
    public Boolean allocateTask(Task task) throws Exception {
        // 得到蜘蛛
        Set<Spider> spiders = getAllSpiders(task.getSpiderType());
        if (spiders == null || spiders.isEmpty()) {
            throw new Exception();
        }
        // 选择蜘蛛
        Spider spider = chooseSpider(spiders, task);
        Boolean result = addTask(spider, task);
        return result;
    }

    /**
     * 从所有spider种获取一个
     *
     * @param spiders
     * @param task
     * @return
     */
    public abstract Spider chooseSpider(Set<Spider> spiders, Task task);

    /**
     * 获取所有存活的Spider
     *
     * @return
     */
    public abstract Set<Spider> getAllSpiders(SpiderType type);

    /**
     * 添加一个任务
     *
     * @param spider
     * @param task
     * @return
     */
    public abstract Boolean addTask(Spider spider, Task task);

    public SpiderMonitor getSpiderMonitor() {
        return spiderMonitor;
    }

    public void setSpiderMonitor(SpiderMonitor spiderMonitor) {
        this.spiderMonitor = spiderMonitor;
    }

    public IDistributeStrategy getDistributeStrategy() {
        return distributeStrategy;
    }

    public void setDistributeStrategy(IDistributeStrategy distributeStrategy) {
        this.distributeStrategy = distributeStrategy;
    }

    public ITaskRepertory getTaskRepertory() {
        return taskRepertory;
    }

    public void setTaskRepertory(ITaskRepertory taskRepertory) {
        this.taskRepertory = taskRepertory;
    }
}
