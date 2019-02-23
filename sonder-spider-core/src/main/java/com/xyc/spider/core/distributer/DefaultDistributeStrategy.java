package com.xyc.spider.core.distributer;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.Task;
import com.xyc.spider.core.utils.ConsistentHash;

import java.util.Set;

/**
 * @description: 采用一致性算法分配任务
 * @author: xyc
 * @create: 2019-02-02 16:56
 */
public class DefaultDistributeStrategy implements IDistributeStrategy {
    private int numberOfReplicas;// 虚拟节点个数
    public DefaultDistributeStrategy(int numberOfReplicas) {
        super();
        this.numberOfReplicas = numberOfReplicas;
    }

    /**
     * 根据一致性哈希算法找到相应的Spider
     *
     * @param task
     * @return
     */
    @Override
    public Spider chooseSpider(Set<Spider> spiders, Task task) {
        return new ConsistentHash<>(numberOfReplicas, spiders).get(task);
    }


}
