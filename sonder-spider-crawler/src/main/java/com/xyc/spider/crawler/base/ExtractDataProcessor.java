package com.xyc.spider.crawler.base;

import com.xyc.spider.crawler.ExtractFields;
import com.xyc.spider.crawler.ExtractSeeds;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-06 00:11
 */
public abstract class ExtractDataProcessor implements PageProcessor {
    // 提取链接规则
    private ExtractFields extractFields;
    //提取种子规则
    private ExtractSeeds extractSeeds;


    @Override
    public void process(Page page) {
        extractFields.extractFields(page);
        extractSeeds.extractSeeds(page);
    }

    @Override
    public Site getSite() {
        return null;
    }

    public ExtractFields getExtractFields() {
        return extractFields;
    }

    public void setExtractFields(ExtractFields extractFields) {
        this.extractFields = extractFields;
    }

    public ExtractSeeds getExtractSeeds() {
        return extractSeeds;
    }

    public void setExtractSeeds(ExtractSeeds extractSeeds) {
        this.extractSeeds = extractSeeds;
    }
}
