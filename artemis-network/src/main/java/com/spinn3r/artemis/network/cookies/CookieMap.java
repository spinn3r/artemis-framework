package com.spinn3r.artemis.network.cookies;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores cookies as a map between String and Cookie.
 */
public class CookieMap extends HashMap<String,Cookie> {

    public CookieMap() {
    }

    public CookieMap(Map<? extends String, ? extends Cookie> m) {
        super(m);
    }

}
