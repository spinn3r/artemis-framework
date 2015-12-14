package com.spinn3r.artemis.corpus.network.test;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.DefaultHttpResponseMeta;
import com.spinn3r.artemis.network.builder.HttpRequestMeta;
import com.spinn3r.artemis.network.builder.HttpResponseMeta;

/**
 *
 */
public class CachedContent {

    private final String key;

    private final String content;

    private final HttpRequestMeta httpRequestMeta;

    private final HttpResponseMeta httpResponseMeta;

    public CachedContent(String key, String content, HttpRequestMeta httpRequestMeta, HttpResponseMeta httpResponseMeta) {
        this.key = key;
        this.content = content;
        this.httpRequestMeta = httpRequestMeta;
        this.httpResponseMeta = httpResponseMeta;
    }

    public String getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public HttpRequestMeta getHttpRequestMeta() {
        return httpRequestMeta;
    }

    public HttpResponseMeta getHttpResponseMeta() {
        return httpResponseMeta;
    }

    @Override
    public String toString() {
        return "CachedContent{" +
                 "key='" + key + '\'' +
                 ", content='" + content + '\'' +
                 ", httpRequestMeta=" + httpRequestMeta +
                 ", httpResponseMeta=" + httpResponseMeta +
                 '}';
    }

}
