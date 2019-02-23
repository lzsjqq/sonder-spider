package com.xyc.spider.core.monitor;

import com.alibaba.fastjson.JSONObject;
import com.xyc.spider.core.base.SpiderConstant;
import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.exception.ExceptionCode;
import com.xyc.spider.core.exception.SonderSpiderException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 19:55
 */
public class SpiderConnectionListener implements ConnectionStateListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String path;
    private Spider data;

    public SpiderConnectionListener(String path, Spider data) {
        this.path = path;
        this.data = data;
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        switch (connectionState){
            case CONNECTED:
                break;
            case SUSPENDED:
                break;
            case RECONNECTED:
                break;
            case LOST:
                lost(curatorFramework);
            case READ_ONLY:
                break;
        }

    }

    /**
     *
     * @param curatorFramework
     */
    private void lost(CuratorFramework curatorFramework) {
        logger.error("[负载均衡失败]zk session超时");
        while (true) {
            try {
                if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                    if (data == null) {
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
                    } else {
                        data.setRegisterTime(System.currentTimeMillis());
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, JSONObject.toJSONString(data).getBytes(SpiderConstant.DEFAULT_CHARSET));
                    }
                    logger.info("[负载均衡修复]重连zk成功");
                    break;
                }
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                throw new SonderSpiderException(ExceptionCode.ERROR, e);
            }
        }
    }
}
