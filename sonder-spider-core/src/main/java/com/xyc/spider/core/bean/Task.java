package com.xyc.spider.core.bean;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-02 16:28
 */
public class Task {
    private String url;

    private SpiderType spiderType;

    @Override
    public String toString() {
        return url;
    }

    public SpiderType getSpiderType() {
        return spiderType;
    }

    public void setSpiderType(SpiderType spiderType) {
        this.spiderType = spiderType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
