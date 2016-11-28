package com.spinn3r.artemis.network.builder.proxies;

/**
 *
 */
public class ProxyMeta {

    private final String host;

    private final int port;

    private final String country;

    private final String provider;

    public ProxyMeta(String host, int port, String country, String provider) {
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
