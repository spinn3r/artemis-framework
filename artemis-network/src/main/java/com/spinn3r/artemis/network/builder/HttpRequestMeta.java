package com.spinn3r.artemis.network.builder;

/**
 *
 */
public class HttpRequestMeta {

    private String resource;

    public HttpRequestMeta(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return "HttpRequestMeta{" +
                 "resource='" + resource + '\'' +
                 '}';
    }

}
