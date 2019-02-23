package com.xyc.spider.crawler.base;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-05 23:45
 */
public class SpiderTask {
    //父任务id，主要用于检测任务执行情况
    private String parentTaskId;
    private String taskId;
    private Site site;// 只存一次redis

    // 定义抽取field的规则，使用map存储
    private String extractFieldsRule;
    // 定义抽取链接的规则
    private String extractSeedsRule;
    // 定义数据存储
    private String pipelines;
    // 任务链接
    private String url;

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getExtractFieldsRule() {
        return extractFieldsRule;
    }

    public void setExtractFieldsRule(String extractFieldsRule) {
        this.extractFieldsRule = extractFieldsRule;
    }

    public String getExtractSeedsRule() {
        return extractSeedsRule;
    }

    public void setExtractSeedsRule(String extractSeedsRule) {
        this.extractSeedsRule = extractSeedsRule;
    }

    public String getPipelines() {
        return pipelines;
    }

    public void setPipelines(String pipelines) {
        this.pipelines = pipelines;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
