package com.xyc.spider.core.bean;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 18:48
 */
public enum SpiderType {
    DEFAULT("default"),
    INCREMENT("increment");
    private String nodeName;
    SpiderType(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
