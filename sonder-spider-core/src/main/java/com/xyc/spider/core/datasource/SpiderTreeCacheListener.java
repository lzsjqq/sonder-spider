package com.xyc.spider.core.datasource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @description:处理
 * @author: xyc
 * @create: 2019-02-03 17:03
 */
public class SpiderTreeCacheListener implements TreeCacheListener {
    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

    }
}
