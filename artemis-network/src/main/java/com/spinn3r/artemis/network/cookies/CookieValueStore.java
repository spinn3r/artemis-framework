package com.spinn3r.artemis.network.cookies;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class CookieValueStore extends ArrayList<CookieValueMap> {

    public CookieValueStore(int initialCapacity) {
        super(initialCapacity);
    }

    public CookieValueStore() {
    }

    public CookieValueStore(Collection<? extends CookieValueMap> c) {
        super(c);
    }

}
