package com.spinn3r.artemis.network.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.URLResourceRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkConfig {

    private String userAgent;

    private boolean blockSSL;

    private String defaultProxy;

    private Map<String,ProxySettings> proxies = new HashMap<>();

    private int defaultMaxContentLength = URLResourceRequest.MAX_CONTENT_LENGTH;

    private int defaultReadTimeout = ResourceRequestFactory.DEFAULT_READ_TIMEOUT;

    private int defaultConnectTimeout = ResourceRequestFactory.DEFAULT_CONNECT_TIMEOUT;

    private boolean requireProxy = false;

    private Map<String,RequestSettings> requests = new HashMap<>();

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean getBlockSSL() {
        return blockSSL;
    }

    public void setBlockSSL(boolean blockSSL) {
        this.blockSSL = blockSSL;
    }

    public String getDefaultProxy() {
        return defaultProxy;
    }

    public void setDefaultProxy(String defaultProxy) {
        this.defaultProxy = defaultProxy;
    }

    public Map<String, ProxySettings> getProxies() {
        return proxies;
    }

    public int getDefaultMaxContentLength() {
        return defaultMaxContentLength;
    }

    public void setDefaultMaxContentLength(int defaultMaxContentLength) {
        this.defaultMaxContentLength = defaultMaxContentLength;
    }

    public int getDefaultReadTimeout() {
        return defaultReadTimeout;
    }

    public void setDefaultReadTimeout(int defaultReadTimeout) {
        this.defaultReadTimeout = defaultReadTimeout;
    }

    public int getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public boolean getRequireProxy() {
        return requireProxy;
    }

    public void setRequireProxy(boolean requireProxy) {
        this.requireProxy = requireProxy;
    }

    public Map<String, RequestSettings> getRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return "NetworkConfig{" +
                 "userAgent='" + userAgent + '\'' +
                 ", blockSSL=" + blockSSL +
                 ", defaultProxy='" + defaultProxy + '\'' +
                 ", proxies=" + proxies +
                 ", defaultMaxContentLength=" + defaultMaxContentLength +
                 ", defaultReadTimeout=" + defaultReadTimeout +
                 ", defaultConnectTimeout=" + defaultConnectTimeout +
                 ", requireProxy=" + requireProxy +
                 ", requests=" + requests +
                 '}';
    }

}



