package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.cookies.SetCookieDescription;

import java.net.CookieManager;
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
public class ThreadLocalCookieStore implements CookieStore {

    private final ThreadLocal<CookieStore> stores;

    public ThreadLocalCookieStore(List<SetCookieDescription> setCookieDescriptions) {

        CookieStore cookieStore = new CookieManager().getCookieStore(); // new InMemoryCookieStore();

        setCookieDescriptions.stream().forEach(setCookieDescription -> {

            List<HttpCookie> httpCookies = HttpCookie.parse(setCookieDescription.getSetCookie());

            httpCookies.stream().forEach(httpCookie ->{

                httpCookie.setDomain(setCookieDescription.getDomain());
                String path = httpCookie.getPath();
                if (path == null || path.isEmpty()) {
                    httpCookie.setPath("/");
                }

                cookieStore.add(null, httpCookie);

            });
        });

        stores = new ThreadLocal<CookieStore>() {
            @Override
            protected synchronized CookieStore initialValue() {
                return cookieStore;
            }
        };
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        getStore().add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        CookieStore store = getStore();
        return store.get(uri);
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
        CookieStore cookieStore = stores.get();
        return cookieStore;
    }

    public void purgeStore() {
        stores.remove();
    }
}