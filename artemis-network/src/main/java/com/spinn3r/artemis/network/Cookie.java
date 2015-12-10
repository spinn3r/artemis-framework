package com.spinn3r.artemis.network;

/**
 *
 */
public class Cookie {

    private final String name;

    private final String value;

    private final String path;

    private final String domain;

    private final boolean httpOnly;

    public Cookie(String name, String value, String path, String domain, boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.path = path;
        this.domain = domain;
        this.httpOnly = httpOnly;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    public String getDomain() {
        return domain;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                 "name='" + name + '\'' +
                 ", value='" + value + '\'' +
                 ", path='" + path + '\'' +
                 ", domain='" + domain + '\'' +
                 ", httpOnly=" + httpOnly +
                 '}';
    }

}
