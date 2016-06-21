package com.spinn3r.artemis.network.cookies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.HttpCookie;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 *
 */
public class Cookie {

    private String name;

    private String value;

    private Optional<String> path;

    private Optional<String> domain;

    private boolean httpOnly;

    private boolean secure;

    private Optional<Long> maxAge;

    public Cookie(String name, String value) {
        this(name, value, Optional.of("/"), Optional.empty(), false, false, Optional.empty() );
    }

    public Cookie(String name,
                  String value,
                  Optional<String> path,
                  Optional<String> domain,
                  boolean httpOnly,
                  boolean secure,
                  Optional<Long> maxAge) {
        this.name = name;
        this.value = value;
        this.path = path;
        this.domain = domain;
        this.httpOnly = httpOnly;
        this.secure = secure;
        this.maxAge = maxAge;
    }

    public Cookie( @JsonProperty("name") String name,
                   @JsonProperty("value") String value,
                   @JsonProperty("path") String path,
                   @JsonProperty("domain") String domain,
                   @JsonProperty("httpOnly") boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.path = Optional.ofNullable(path);
        this.domain = Optional.ofNullable(domain);
        this.httpOnly = httpOnly;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    /**
     * If the cookie's path is not specified it defaults to the path requested.
     * @return
     */
    public Optional<String> getPath() {
        return path;
    }

    /**
     * If a cookie's domain and path are not specified by the server, they
     * default to the domain and path of the resource that was requested
     * However, in most browsers there is a difference between a cookie set
     * from foo.com without a domain, and a cookie set with the foo.com domain.
     * In the former case, the cookie will only be sent for requests to foo.com.
     * In the latter case, all sub domains are also included (for example,
     * docs.foo.com).[31][32] A notable exception to this general rule is
     * Internet Explorer, which always sends cookies to sub domains regardless
     * of whether the cookie was set with or without a domain.[33]
     */
    public  Optional<String> getDomain() {
        return domain;
    }

    /**
     * Cookies with httpOnly flag can only be used when transmitted via HTTP (or
     * HTTPS). They are not accessible through non-HTTP APIs such as JavaScript.
     * This restriction eliminates the threat of cookie theft via cross-site
     * scripting (XSS), while leaving the threats of cross-site tracing (XCT)
     * and cross-site request forgery (CSRF) intact.
     *
     * The HttpOnly attribute directs browsers not to expose cookies through
     * channels other than HTTP (and HTTPS) requests. Cookies with this
     * attribute are not accessible via non-HTTP methods, such as calls via
     * JavaScript (using document.cookie), and therefore cannot be stolen easily
     * via cross-site scripting (a pervasive attack technique).[37]
     *
     */
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * The Secure attribute is meant to keep cookie communication limited to
     * encrypted transmission, directing browsers to use cookies only via
     * secure/encrypted connections. However, if a web server sets a cookie with
     * a secure attribute from a non-secure connection, the cookie can still be
     * intercepted when it is sent to the user by man-in-the-middle attacks.
     * Therefore, for maximum security, cookies with the Secure attribute should
     * only be set over a secure connection.
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * The Expires attribute defines a specific date and time for when the
     * browser should delete the cookie. The date/time is specified in the form
     * Wdy, DD Mon YYYY HH:MM:SS GMT.[35]
     *
     * Alternatively, the Max-Age attribute can be used to set the cookie’s
     * expiration as an interval of seconds in the future, relative to the time
     * the browser received the cookie. Below is an example of three Set-Cookie
     * headers that were received from a website after a user logged in:
     *
     * Set-Cookie: lu=Rg3vHJZnehYLjVg7qi3bZjzg; Expires=Tue, 15 Jan 2013 21:47:38 GMT; Path=/; Domain=.example.com; HttpOnly
     * Set-Cookie: made_write_conn=1295214458; Path=/; Domain=.example.com
     * Set-Cookie: reg_fb_gate=deleted; Expires=Thu, 01 Jan 1970 00:00:01 GMT; Path=/; Domain=.example.com; HttpOnly
     */
    public Optional<Long> getMaxAge() {
        return maxAge;
    }

    public HttpCookie toHttpCookie() {

        HttpCookie httpCookie = new HttpCookie(getName(), getValue());

        path.ifPresent(httpCookie::setPath);
        domain.ifPresent(httpCookie::setDomain);
        maxAge.ifPresent(httpCookie::setMaxAge);
        httpCookie.setHttpOnly(httpOnly);
        httpCookie.setSecure(secure);

        return httpCookie;

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
                 '}';
    }

    public static class Builder {

        private String name;

        private String value;

        private Optional<String> path;

        private Optional<String> domain;

        private boolean httpOnly = false;

        private boolean secure = false;

        private Optional<Long> maxAge;

        public Builder(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setPath(Optional<String> path) {
            this.path = path;
            return this;
        }

        public Builder setDomain(Optional<String> domain) {
            this.domain = domain;
            return this;
        }

        public Builder setHttpOnly(boolean httpOnly) {
            this.httpOnly = httpOnly;
            return this;
        }

        public Builder setSecure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public Builder setMaxAge(Optional<Long> maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        public Cookie build() {
            return new Cookie(name, value, path, domain, httpOnly, secure, maxAge);
        }

    }

}
