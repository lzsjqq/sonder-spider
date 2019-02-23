package com.xyc.spider.core.datasource;

import com.alibaba.fastjson.JSONObject;
import com.xyc.spider.core.base.ISonderSpiderConfig;
import com.xyc.spider.core.base.SpiderConfig;
import com.xyc.spider.core.base.SpiderConstant;
import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.exception.ExceptionCode;
import com.xyc.spider.core.utils.SonderAssert;
import com.xyc.spider.core.exception.SonderSpiderException;
import com.xyc.spider.core.utils.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 14:25
 */
public class ZkCuratorServer {
    private static final Logger logger = LoggerFactory.getLogger(ZkCuratorServer.class);
    private CuratorFramework client;
    private static ZkCuratorServer curatorServer;
    private String zookeeperHosts;
    private int maxRetries;
    private static Integer sessionTimeoutMs;// 毫秒
    private static Integer connectionTimeoutMs;// 毫秒

    private ZkCuratorServer(ISonderSpiderConfig spiderConfig) {
        zookeeperHosts = spiderConfig.getString("zookeeper.hosts");
        maxRetries = 3;
        sessionTimeoutMs = spiderConfig.getInt("spider.node.session.timeoutMs");
        if (sessionTimeoutMs == null) {
            // 十秒
            sessionTimeoutMs = 10 * 1000;
        }
        connectionTimeoutMs = spiderConfig.getInt("spider.node.connection.timeoutMs");
        if (connectionTimeoutMs == null) {
            connectionTimeoutMs = 10 * 1000;
        }
        this.getConnection(sessionTimeoutMs, connectionTimeoutMs);
    }

    public static ZkCuratorServer getInstance(ISonderSpiderConfig spiderConfig) {
        if (curatorServer == null) {
            synchronized (ZkCuratorServer.class) {
                if (curatorServer == null) {
                    curatorServer = new ZkCuratorServer(spiderConfig);
                }
            }
        }
        return curatorServer;
    }

    /**
     * @return
     */
    public ZkCuratorServer getConnection(int sessionTimeoutMs, int connectionTimeoutMs) {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, this.maxRetries);
        this.client = CuratorFrameworkFactory.newClient(this.zookeeperHosts, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();
        return this;
    }

    /**
     * @param stateListener
     */
    public ZkCuratorServer addConnectionStateListener(ConnectionStateListener stateListener) {
        client.getConnectionStateListenable().addListener(stateListener);
        return this;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    public ZkCuratorServer getConnection() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, this.maxRetries);
        this.client = CuratorFrameworkFactory.newClient(this.zookeeperHosts, retryPolicy);
        client.start();
        return this;
    }

    public String getState() {
        return this.client.getState().name();
    }


    /**
     * 创建顺序临时节点
     *
     * @param nodeNames
     * @param data
     * @return 节点path
     */
    public String createEphemeralSequentialWithData(String[] nodeNames, String data) {
        return this.createNodeWithArr(CreateMode.EPHEMERAL_SEQUENTIAL, nodeNames, data);
    }

    public String createEphemeralSequentialWithData(Object data, String[] nodeNames) {
        SonderAssert.isNull(data);
        return this.createNodeWithArr(CreateMode.EPHEMERAL_SEQUENTIAL, nodeNames, JSONObject.toJSONString(data));
    }

    public String createEphemeralSequentialWithData(Object data, String path) {
        SonderAssert.isNull(data);
        return this.createNodeWithPath(CreateMode.EPHEMERAL_SEQUENTIAL, JSONObject.toJSONString(data), path);
    }

    /**
     * @param nodeNames
     * @return
     */
    public String createEphemeralSequentialWithoutData(String[] nodeNames) {
        return this.createNodeWithArr(CreateMode.EPHEMERAL_SEQUENTIAL, nodeNames, null);
    }

    public String createEphemeralWithoutData(String[] nodeNames) {
        return this.createNodeWithArr(CreateMode.EPHEMERAL, nodeNames, null);
    }

    public String createEphemeralWithoutData(String path) {
        return this.createNodeWithPath(CreateMode.EPHEMERAL, null, path);
    }

    public String createEphemeralWithData(Object data, String path) {
        SonderAssert.isNull(data);
        return this.createNodeWithPath(CreateMode.EPHEMERAL, JSONObject.toJSONString(data), path);
    }

    /**
     * 创建永久节点
     *
     * @param data
     * @param path
     * @return
     */
    public String createPersistentWithData(Object data, String path) {
        SonderAssert.isNull(data);
        return createNodeWithPath(CreateMode.PERSISTENT, JSONObject.toJSONString(data), path);
    }

    /**
     * 创建永久节点
     *
     * @param path
     * @return
     */
    public String createPersistentWithoutData(String path) {
        SonderAssert.isNull(path);
        return createNodeWithPath(CreateMode.PERSISTENT, null, path);
    }

    public String createPersistentWithoutData(String[] nodeNames) {
        SonderAssert.arrIsEmpty(nodeNames);
        return createNodeWithArr(CreateMode.PERSISTENT, nodeNames, null);
    }

    /**
     * @param mode
     * @param data
     * @param nodeNames
     * @return
     */
    public String createNodeWithArr(CreateMode mode, String[] nodeNames, String data) {
        SonderAssert.arrIsEmpty(nodeNames);
        StringBuilder path = new StringBuilder("/");
        for (int i = 0; i < nodeNames.length; i++) {
            String next = nodeNames[i];
            if (i == nodeNames.length - 1) {
                path.append(next);
            } else {
                path.append(next + "/");
            }
        }
        return createNodeWithPath(mode, data, path.toString());
    }

    public String createNodeWithPath(CreateMode mode, String data, String path) {
        SonderAssert.isBlank(path);
        byte[] dataByte;
        String result;
        try {
            // 检查节点是否存在
            ExistsBuilder existsBuilder = this.client.checkExists();
            Stat stat = existsBuilder.forPath(path);
            // 存在直接返回
            if (stat != null) {
                return path;
            }
            if (StringUtils.isBlank(data)) {
                dataByte = null;
            } else {
                dataByte = data.getBytes(SpiderConstant.DEFAULT_CHARSET);
            }
            if (dataByte == null) {
                result = this.client.create().creatingParentsIfNeeded().withMode(mode).forPath(path);
            } else {
                result = this.client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, dataByte);
            }
        } catch (Exception e) {
            throw new SonderSpiderException(ExceptionCode.ERROR, e);
        }
        return result;
    }

    /**
     * Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据
     *
     * @param listener
     * @param path
     * @throws Exception
     */
    public void createTreeCacheWatch(TreeCacheListener listener, String path) throws Exception {
        TreeCache watcher = new TreeCache(this.client, path);
        watcher.getListenable().addListener(listener);
        watcher.start();
    }

    public static void main(String args[]) throws Exception {
        ZkCuratorServer curatorServer = ZkCuratorServer.getInstance(SpiderConfig.getInstance());
        String[] nodeNames = {"parent", "001"};
        String path = curatorServer.getConnection(10, 1000)
                .createEphemeralSequentialWithoutData(nodeNames);
        System.out.println(path);
    }

    /**
     * @param client
     * @param path
     */
    private void dowork(CuratorFramework client, String path) {
        InterProcessMutex ipm = new InterProcessMutex(client, path);
        try {
            ipm.acquire();
            System.out.println("eeee");
            logger.info("Thread ID:" + Thread.currentThread().getId() + " acquire the lock");
            Thread.sleep(1000);
        } catch (Exception e) {

        } finally {
            try {
                ipm.release();
                logger.info("Thread ID:" + Thread.currentThread().getId() + " release the lock");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保持会话同时刷新状态
     *
     * @param path
     * @throws Exception
     */
    public void keepClientForSpider(String path) throws Exception {
        String now = String.valueOf(System.currentTimeMillis());
        curatorServer.getClient().setData().forPath(path, now.getBytes(SpiderConstant.DEFAULT_CHARSET));
        logger.info("keep client alive ：{}", now);
        Thread.sleep(sessionTimeoutMs / 2);
    }

    /**
     * 添加客户端监听
     *
     * @param connectionListener
     */
    public void addClientListener(ConnectionStateListener connectionListener) {
        client.getConnectionStateListenable().addListener(connectionListener);
    }

    /**
     * 获取node数据
     *
     * @param parentNodePath
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Set<T> getNodeDate(String parentNodePath, Class<T> clazz) throws Exception {
        Set<T> dataSet = null;
        List<String> childPaths = client.getChildren().forPath(parentNodePath);
        if (CollectionUtil.isNotNullOrEmpty(childPaths)) {
            dataSet = Collections.synchronizedSet(new HashSet<>(childPaths.size()));
            for (String path : childPaths) {
                byte[] bytes = client.getData().forPath(parentNodePath + "/" + path);
                dataSet.add(JSONObject.parseObject(StringUtils.toString(bytes, SpiderConstant.DEFAULT_CHARSET), clazz));
            }
        }
        return dataSet;
    }

    /**
     * @param spider
     * @param nodePath
     */
    public boolean setDate(Spider spider, String nodePath) throws Exception {
        Stat stat = client.setData().forPath(nodePath, JSONObject.toJSONString(spider).getBytes(SpiderConstant.DEFAULT_CHARSET));
        if (stat != null) {
            return true;
        } else {
            return false;
        }
    }
}
