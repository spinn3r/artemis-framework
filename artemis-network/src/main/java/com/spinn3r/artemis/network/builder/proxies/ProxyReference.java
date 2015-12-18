package com.spinn3r.artemis.network.builder.proxies;

import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ProxyReference {

    private final String host;

    private final int port;

    private final Proxy proxy;

    public ProxyReference(String host, int port, Proxy proxy) {
        this.host = host;
        this.port = port;
        this.proxy = proxy;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public String toString() {
        return "ProxyReference{" +
                 "host='" + host + '\'' +
                 ", port=" + port +
                 ", proxy=" + proxy +
                 '}';
    }

}
