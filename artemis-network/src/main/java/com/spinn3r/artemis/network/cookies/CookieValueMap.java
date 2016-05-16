package com.spinn3r.artemis.network.cookies;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores a map (as strings) between cookie name and cookie value.
 */
public class CookieValueMap extends HashMap<String,String> {

    public CookieValueMap() {
    }

    public CookieValueMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

}
