package com.xyc.spider.core.bean;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-02 16:42
 */
public class Spider {
    private SpiderType spiderType;
    private Long registerTime;
    private String nodeName;
    private String host;//所在主机名
    private String pid;// spider的进程

    public SpiderType getSpiderType() {
        return spiderType;
    }

    public void setSpiderType(SpiderType spiderType) {
        this.spiderType = spiderType;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
