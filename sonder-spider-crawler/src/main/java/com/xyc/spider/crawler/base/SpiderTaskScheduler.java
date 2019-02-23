package com.xyc.spider.crawler.base;

import com.alibaba.fastjson.JSONObject;
import com.xyc.spider.core.base.SpiderConfig;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import com.xyc.spider.core.datasource.JedisDao;

import java.util.List;

/**
 * 1.任务链接从任务列表获取
 * 2.任务表自己生成，分发到不同的主机执行
 * 3.对原始种子做监控
 * 4.按照网站作为key
 * 5.对任务进行过滤
 *
 * @description: 执行任务的具体实例
 * @author: xyc
 * @create: 2019-02-05 21:54
 */
public class SpiderTaskScheduler {
    private String hostName;

    public void xx() {

    }

    public void run() {
        JedisDao jedisDao = JedisDao.getInstance(SpiderConfig.getInstance());
        while (true) {
            String val = jedisDao.get(hostName + ":xx");
            JSONObject jsonObject = JSONObject.parseObject(val);
            // 1.不能访问太多的数据库
            // 2.定义好下载方式
            // 3.定义对下载好的数据的处理方式
            // 4.定义好提取链接的方式
            // 5.编写主类

        }
    }

}
