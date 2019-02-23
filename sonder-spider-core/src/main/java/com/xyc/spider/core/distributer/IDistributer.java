package com.xyc.spider.core.distributer;

import com.xyc.spider.core.bean.Task;

/**
 * @description: 任务分配中心
 * @author: xyc
 * @create: 2019-02-02 16:16
 */
public interface IDistributer {
    // 分配任务
    Boolean allocateTask(Task task) throws Exception;


}
