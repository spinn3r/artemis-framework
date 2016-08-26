package com.spinn3r.artemis.http.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebserverConfig {

    private int port;

    private int maxThreads = 500;

    private boolean useLocalHost = false;

    private int requestHeaderSize = 128 * 1024;

    private int responseHeaderSize = 128 * 1024;

    private boolean enableCompression = false;

    public WebserverConfig() {}

    public WebserverConfig(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public boolean getUseLocalHost() {
        return useLocalHost;
    }

    public int getRequestHeaderSize() {
        return requestHeaderSize;
    }

    public int getResponseHeaderSize() {
        return responseHeaderSize;
    }

    public boolean getEnableCompression() {
        return enableCompression;
    }

    @Override
    public String toString() {
        return "WebserverConfig{" +
                 "port=" + port +
                 ", maxThreads=" + maxThreads +
                 ", useLocalHost=" + useLocalHost +
                 ", requestHeaderSize=" + requestHeaderSize +
                 ", responseHeaderSize=" + responseHeaderSize +
                 ", enableCompression=" + enableCompression +
                 '}';
    }

}
