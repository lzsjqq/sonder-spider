package com.xyc.spider.core.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 10:45
 */
public class SonderSpiderException extends RuntimeException {

    public SonderSpiderException() {
        super();
    }

    public SonderSpiderException(ExceptionCode exceptionCode) {
        super(JSONObject.toJSONString(exceptionCode));
    }

    public SonderSpiderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SonderSpiderException(Throwable cause) {
        super(cause);
    }

    public SonderSpiderException(ExceptionCode exceptionCode, Throwable cause) {
        super(JSONObject.toJSONString(exceptionCode), cause);
    }

}
