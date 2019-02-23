package com.xyc.spider.core.monitor;

import com.alibaba.fastjson.JSONObject;
import com.xyc.spider.core.base.ISonderSpiderConfig;
import com.xyc.spider.core.base.SpiderConstant;
import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;
import com.xyc.spider.core.datasource.ZkCuratorServer;
import com.xyc.spider.core.exception.ExceptionCode;
import com.xyc.spider.core.exception.SonderSpiderException;
import com.xyc.spider.core.utils.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 使用zk作为集群的监控中心，一台服务器只允许获取一个该实例
 * @author: xyc
 * @create: 2019-02-02 19:09
 */
public class ZookeeperSpiderMonitor extends SpiderMonitor implements TreeCacheListener {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperSpiderMonitor.class);
    private static ZookeeperSpiderMonitor zookeeperSpiderMonitor;
    private static ISonderSpiderConfig spiderConfig;
    private static Map<SpiderType, String> parentNodePathMap = Collections.synchronizedMap(new HashMap<>());
    // 是否第一次运行，用于保持会话
    private static boolean isJustStartRun = true;
    private static String rootNodeName;// 毫秒
    private Map<SpiderType, Set<Spider>> nodeDate = Collections.synchronizedMap(new HashMap<>());
    private static final Long GET_NODE_LIMIT = 10 * 60 * 1000L;
    private static Long lastGetTime = System.currentTimeMillis();

    private static Lock reentrantLock = new ReentrantLock();
    private static Condition condition = reentrantLock.newCondition();


    private ZookeeperSpiderMonitor(ISonderSpiderConfig config) {
        spiderConfig = config;
        rootNodeName = spiderConfig.getString("spider.monitor.parentNode");
        if (StringUtils.isBlank(rootNodeName)) {
            rootNodeName = "spider";
        }
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {

        }, GET_NODE_LIMIT, GET_NODE_LIMIT, TimeUnit.SECONDS);
    }

    public static ZookeeperSpiderMonitor getInstance(ISonderSpiderConfig config) {
        if (zookeeperSpiderMonitor == null) {
            synchronized (SpiderMonitor.class) {
                if (zookeeperSpiderMonitor == null) {
                    zookeeperSpiderMonitor = new ZookeeperSpiderMonitor(config);
                }
            }
        }
        return zookeeperSpiderMonitor;
    }


    @Override
    public Set<Spider> getAllSpiders(SpiderType spiderType) {
        return getAllSpidersByType(spiderType);
    }

    private Set<Spider> getAllSpidersByType(SpiderType spiderType) {
        try {
            // 上锁
            reentrantLock.lock();
            Set<Spider> spiders = nodeDate.get(spiderType);
            long now = System.currentTimeMillis();
            if (CollectionUtil.isNullOrEmpty(spiders) || now - GET_NODE_LIMIT > lastGetTime) {
                lastGetTime = now;
                ZkCuratorServer zkCuratorServer = ZkCuratorServer.getInstance(spiderConfig);
                Set<Spider> nodes = zkCuratorServer.getNodeDate(parentNodePathMap.get(spiderType), Spider.class);
                //添加节点
                nodeDate.put(spiderType, nodes);
                logger.info("all spiders:{}", JSONObject.toJSONString(nodeDate));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // 释放锁
            reentrantLock.unlock();
        }
        return nodeDate.get(spiderType);
    }

    @Override
    public Boolean register(Spider spider) throws Exception {

        if (null == spider || null == spider.getSpiderType()) {
            spider = new Spider();
            spider.setSpiderType(SpiderType.DEFAULT);
        }
        spider.setRegisterTime(System.currentTimeMillis());
        // 创建客户端
        final ZkCuratorServer curatorServer = ZkCuratorServer.getInstance(spiderConfig);
        String nodeName = spider.getSpiderType().getNodeName();
        // 创建永久节点
        String persistentPath = curatorServer.createPersistentWithoutData(new String[]{rootNodeName, nodeName});
        // 添加父节点
        parentNodePathMap.put(spider.getSpiderType(), persistentPath);
        // 创建临时节点
        String nodePath = curatorServer.createEphemeralSequentialWithoutData(new String[]{rootNodeName, nodeName, nodeName});
        // 为节点赋值
        spider.setNodeName(nodePath);
        if (!curatorServer.setDate(spider, nodePath)) {
            throw new SonderSpiderException(ExceptionCode.SPIDER_NODE_ADD_ERROR);
        }
        //添加client监听
        curatorServer.addClientListener(new SpiderConnectionListener(nodePath, spider));
        // 创建节点状态监听
        curatorServer.createTreeCacheWatch(zookeeperSpiderMonitor, nodePath);
        //保持会话
        if (isJustStartRun) {
            new Thread(() -> {
                try {
                    while (true) {
                        curatorServer.keepClientForSpider(persistentPath);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }).start();
        }
        logger.info("registerNode isJustStartRun:{} spider:{} ", isJustStartRun, JSONObject.toJSONString(spider));
        return true;
    }

    /**
     * @param bytes
     * @return
     * @throws UnsupportedEncodingException
     */
    private static Spider getSpider(byte[] bytes) throws UnsupportedEncodingException {
        return JSONObject.parseObject(StringUtils.toString(bytes, SpiderConstant.DEFAULT_CHARSET), Spider.class);
    }


    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
        TreeCacheEvent.Type type = treeCacheEvent.getType();
        switch (type) {
            case INITIALIZED:
                // TODO
                logger.info(treeCacheEvent.toString());
                return;
            case NODE_UPDATED:
                // TODO
                logger.info(treeCacheEvent.toString());
                return;
            default:
                byte[] data = treeCacheEvent.getData().getData();
                getAllSpidersByType(getSpider(data).getSpiderType());
                logger.info(treeCacheEvent.toString());
                return;
        }
    }

}
