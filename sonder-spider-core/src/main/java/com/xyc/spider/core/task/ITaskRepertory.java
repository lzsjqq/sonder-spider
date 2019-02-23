package com.xyc.spider.core.task;

import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.Task;

/**
 * @description: 任务的存储仓库
 * @author: xyc
 * @create: 2019-02-02 16:22
 */
public interface ITaskRepertory {

    /**
     * @return
     */
    Boolean addTask(Spider spider, Task task);

}
