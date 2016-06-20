package com.spinn3r.artemis.network.builder.cookies;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.network.cookies.Cookie;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class ThreadLocalCookies {

    private final Provider<ThreadLocalCookieStore> threadLocalCookieStoreProvider;

    @Inject
    ThreadLocalCookies(Provider<ThreadLocalCookieStore> threadLocalCookieStoreProvider) {
        this.threadLocalCookieStoreProvider = threadLocalCookieStoreProvider;
    }

    /**
     * Flush the cookies used to perform HTTP requests in this thread..
     */
    public void flush() {

        ThreadLocalCookieStore threadLocalCookieStore = threadLocalCookieStoreProvider.get();
        CookieStore cookieStore = threadLocalCookieStore.get();
        cookieStore.removeAll();

    }

    public ImmutableList<HttpCookie> getHttpCookies() {
        return ImmutableList.copyOf( threadLocalCookieStoreProvider.get().get().getCookies() );
    }

    public ImmutableList<Cookie> getCookies() {

        List<Cookie> result = Lists.newArrayList();

        for (HttpCookie httpCookie : getHttpCookies()) {

            Cookie cookie = new Cookie(httpCookie.getName(),
                                       httpCookie.getValue(),
                                       Optional.ofNullable( httpCookie.getPath()),
                                       Optional.ofNullable(httpCookie.getDomain()),
                                       ! httpCookie.getSecure(),
                                       httpCookie.getSecure(),
                                       Optional.of(httpCookie.getMaxAge()));

            result.add(cookie);

        }

        return ImmutableList.copyOf(result);

    }

}
