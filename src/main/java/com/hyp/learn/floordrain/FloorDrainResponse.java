package com.hyp.learn.floordrain;

import com.hyp.learn.floordrain.utils.RequestUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 响应封装类
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain
 * hyp create at 20-3-24
 **/
public class FloorDrainResponse {
    private int code;
    private String msg;

    private long expire;
    private int limitCount;

    private AccessInfo accessInfo;

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public FloorDrainResponse setAccessInfo(HttpServletRequest request) {
        if (this.accessInfo == null) {
            this.accessInfo = new AccessInfo();
        }
        RequestUtil requestUtil = new RequestUtil(request);
        this.accessInfo.setIp(requestUtil.getIp())
                .setParams(requestUtil.getParameters())
                .setReferer(requestUtil.getReferer())
                .setRequestUrl(requestUtil.getRequestUrl())
                .setUa(requestUtil.getUa());
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public FloorDrainResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public long getExpire() {
        return expire;
    }

    FloorDrainResponse setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public FloorDrainResponse setLimitCount(int limitCount) {
        this.limitCount = limitCount;
        return this;
    }

    public FloorDrainResponse isSuccess() {
        this.code = 1;
        return this;
    }

    public FloorDrainResponse isError() {
        this.code = 0;
        return this;
    }


    /**
     * 访问类
     */
    public class AccessInfo {
        private String ip;
        private String ua;
        private String referer;
        private String requestUrl;
        private String params;

        public String getIp() {
            return ip;
        }

        AccessInfo setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public String getUa() {
            return ua;
        }

        AccessInfo setUa(String ua) {
            this.ua = ua;
            return this;
        }

        public String getReferer() {
            return referer;
        }

        AccessInfo setReferer(String referer) {
            this.referer = referer;
            return this;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        AccessInfo setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public String getParams() {
            return params;
        }

        AccessInfo setParams(String params) {
            this.params = params;
            return this;
        }
    }
}
