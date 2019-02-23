package com.xyc.spider.core.exception;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 10:48
 */
public enum ExceptionCode {
    SPIDER_NODE_ADD_ERROR("Spider node add error!",600),
    SPIDER_NODE_REMOVED("Spider node is removed!", 500),
    IS_BLANK("Blank Is Illegal", 400),
    ERROR_FOR_CONFIG("ERROR_FOR_CONFIG", 300),
    ERROR("ERROR", 100),
    SUCCESS("SUCCESS", 200);

    private String description;
    private int code;

    private ExceptionCode(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
