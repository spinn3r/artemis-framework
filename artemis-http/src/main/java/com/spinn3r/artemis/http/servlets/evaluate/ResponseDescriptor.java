package com.spinn3r.artemis.http.servlets.evaluate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.json.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDescriptor {

    protected int status = 200;

    protected String contentType = "text/plain";

    protected String characterEncoding = Charsets.UTF_8.name().toLowerCase();

    protected ImmutableMap<String,String> headers = ImmutableMap.of();

    protected ImmutableList<ResponseDescriptor.Cookie> cookies = ImmutableList.of();

    public int getStatus() {
        return status;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public ImmutableMap<String, String> getHeaders() {
        return headers;
    }

    public ImmutableList<Cookie> getCookies() {
        return cookies;
    }

    public static class Cookie {

        private String name;

        private String value;

        private Optional<String> path;

        private Optional<String> domain;

        private Optional<Boolean> httpOnly;

        private Optional<Boolean> secure;

        private Optional<Integer> maxAge;

        private Optional<String> expires;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public Optional<String> getPath() {
            return path;
        }

        public Optional<String> getDomain() {
            return domain;
        }

        public Optional<Boolean> getHttpOnly() {
            return httpOnly;
        }

        public Optional<Boolean> getSecure() {
            return secure;
        }

        public Optional<Integer> getMaxAge() {
            return maxAge;
        }

        public Optional<String> getExpires() {
            return expires;
        }

        @Override
        public String toString() {
            return "Cookie{" +
                     "name='" + name + '\'' +
                     ", value='" + value + '\'' +
                     ", path=" + path +
                     ", domain=" + domain +
                     ", httpOnly=" + httpOnly +
                     ", secure=" + secure +
                     ", maxAge=" + maxAge +
                     ", expires=" + expires +
                     '}';
        }

    }

    public String toJSONRecord() {
        return JSON.toJSONRecord(this);
    }

    public String toParam() {
        try {
            return URLEncoder.encode(toJSONRecord(), Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResponseDescriptor parse(String content) {
        return JSON.fromJSON(ResponseDescriptor.class, content);
    }

    public static class Builder {

        ResponseDescriptor responseDescriptor = new ResponseDescriptor();

        public ResponseDescriptor build() {
            return responseDescriptor;
        }

    }

}


