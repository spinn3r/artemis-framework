package com.spinn3r.artemis.network.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxySettings {

    private String host;

    private int port;

    private String regex;

    private int priority;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getRegex() {
        return regex;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "ProxySettings{" +
                 "host='" + host + '\'' +
                 ", port=" + port +
                 ", regex='" + regex + '\'' +
                 ", priority=" + priority +
                 '}';
    }

}
