package com.spinn3r.artemis.network.builder.cookies;

import com.spinn3r.artemis.network.cookies.SetCookieDescription;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

/**
 *
 */
public class ThreadLocalCookieStore extends ThreadLocal<CookieStore> {

    private final List<SetCookieDescription> setCookieDescriptions;

    public ThreadLocalCookieStore(List<SetCookieDescription> setCookieDescriptions) {
        this.setCookieDescriptions = setCookieDescriptions;
    }

    @Override
    protected CookieStore initialValue() {

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

        return cookieStore;

    }

}
