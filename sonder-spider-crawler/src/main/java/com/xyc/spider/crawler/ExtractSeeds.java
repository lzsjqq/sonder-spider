package com.xyc.spider.crawler;

import us.codecraft.webmagic.Page;

import java.util.Map;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-06 00:15
 */
public abstract class ExtractSeeds {
    private Map<String, String> rules;
    private Page page;

    public ExtractSeeds(Page page) {
        this.page = page;
    }

    public abstract Page extractSeeds(Page page);

    public Map<String, String> getRules() {
        return rules;
    }

    public void setRules(Map<String, String> rules) {
        this.rules = rules;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
