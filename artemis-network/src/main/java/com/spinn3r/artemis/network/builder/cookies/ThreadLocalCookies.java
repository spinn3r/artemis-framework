package com.spinn3r.artemis.network.builder.cookies;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.net.CookieStore;

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

}
