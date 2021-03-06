package com.spinn3r.artemis.network.builder.proxies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyMeta implements Comparable<ProxyMeta> {

    private final String host;

    private final int port;

    private final String country;

    private final String provider;

    public ProxyMeta( @JsonProperty("host") String host,
                      @JsonProperty("port") int port,
                      @JsonProperty("country") String country,
                      @JsonProperty("provider") String provider) {
        this.host = host;
        this.port = port;
        this.country = country;
        this.provider = provider;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getCountry() {
        return country;
    }

    public String getProvider() {
        return provider;
    }

    public ProxyReference toProxyReference() {
        return ProxyReferences.create("HTTP", host, port);
    }

    public String format() {
        return String.format("%s:%s", host, port);
    }

    @Override
    public int compareTo(ProxyMeta o) {
        return format().compareTo(o.format());
    }

    @Override
    public String toString() {
        return "ProxyMeta{" +
                 "host='" + host + '\'' +
                 ", port=" + port +
                 ", country='" + country + '\'' +
                 ", provider='" + provider + '\'' +
                 '}';
    }

}
