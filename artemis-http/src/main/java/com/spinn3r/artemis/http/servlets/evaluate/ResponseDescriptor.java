package com.spinn3r.artemis.http.servlets.evaluate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.json.JSON;

import javax.xml.ws.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
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

        protected String name;

        protected String value;

        protected Optional<String> path;

        protected Optional<String> domain;

        protected Optional<Boolean> httpOnly;

        protected Optional<Boolean> secure;

        protected Optional<Integer> maxAge;

        protected Optional<String> expires;

        public Cookie(@JsonProperty("name") String name,
                      @JsonProperty("value") String value) {
            this.name = name;
            this.value = value;
        }

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

        public static class Builder {

            private final Cookie cookie;

            public Builder(String name, String value) {
                cookie = new Cookie(name, value);
            }

            public Builder withPath( String path ) {
                cookie.path = Optional.of(path);
                return this;
            }

            public Builder withDomain( String domain ) {
                cookie.domain = Optional.ofNullable(domain);
                return this;
            }

            public Builder withMaxAge( int maxAge ) {
                cookie.maxAge = Optional.of(maxAge);
                return this;
            }

            public Cookie build() {
                return cookie;

            }

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

        private Map<String,String> headers = Maps.newHashMap();

        private List<Cookie> cookies = Lists.newArrayList();

        public Builder withStatus(int status) {
            responseDescriptor.status = status;
            return this;
        }

        public Builder withHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder withCookie(String name, String value) {
            this.cookies.add(new Cookie(name, value));
            return this;
        }

        public Builder withCookie(ResponseDescriptor.Cookie cookie) {
            this.cookies.add(cookie);
            return this;
        }

        public ResponseDescriptor build() {
            responseDescriptor.headers = ImmutableMap.copyOf(headers);
            responseDescriptor.cookies = ImmutableList.copyOf(cookies);
            return responseDescriptor;

        }

    }

}


