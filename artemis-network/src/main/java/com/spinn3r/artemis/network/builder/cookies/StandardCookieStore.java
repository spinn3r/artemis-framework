package com.spinn3r.artemis.network.builder.cookies;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Taken from :
 *
 * https://raw.githubusercontent.com/kristian/cookie-filter/master/src/main/java/lc/kra/servlet/filter/helper/ThreadLocalCookieStore.java
 *
 */
public class StandardCookieStore implements CookieStore {

    private final ThreadLocalCookieStore threadLocalCookieStore;

    public StandardCookieStore(ThreadLocalCookieStore threadLocalCookieStore) {
        this.threadLocalCookieStore = threadLocalCookieStore;
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        CookieStore store = getStore();
        store.add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        CookieStore store = getStore();
        List<HttpCookie> result = store.get(uri);
        return result;
    }

    @Override
    public List<HttpCookie> getCookies() {
        return getStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return getStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return getStore().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return getStore().removeAll();
    }

    @Override
    public int hashCode() {
        return getStore().hashCode();
    }

    protected CookieStore getStore() {
        CookieStore cookieStore = threadLocalCookieStore.get();
        return cookieStore;
    }

}