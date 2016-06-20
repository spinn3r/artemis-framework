package com.spinn3r.artemis.network.cookies;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SetCookieDescription {

    private final String domain;
    private final String setCookie;

    public SetCookieDescription(@JsonProperty("domain") String domain, @JsonProperty("setCookie") String setCookie) {
        this.domain = domain;
        this.setCookie = setCookie;
    }

    public String getDomain() {
        return domain;
    }

    public String getSetCookie() {
        return setCookie;
    }

    @Override
    public String toString() {
        return "SetCookieDescription{" +
                "domain='" + domain + '\'' +
                ", setCookie='" + setCookie + '\'' +
                '}';
    }
}
