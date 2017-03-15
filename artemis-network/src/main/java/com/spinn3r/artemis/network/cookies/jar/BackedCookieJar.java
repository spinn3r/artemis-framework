package com.spinn3r.artemis.network.cookies.jar;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.cookies.CookieValueMap;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Simple in-memory cookie jar backed by a list containing CookieMap objects.
 */
public class BackedCookieJar implements CookieJar {

    protected final List<CookieValueMap> backing;

    private final Random random = createRandom();

    public BackedCookieJar() {
        this(Lists.newArrayList());
    }

    public BackedCookieJar(List<CookieValueMap> backing) {
        this.backing = backing;
    }

    @Override
    public CookieValueMap getCookies() {
        return backing.get(random.nextInt(backing.size()));
    }

    @Override
    public int size() {
        return backing.size();
    }

    // we don't require secure random for most purposes however we need to make
    // sure robots don't accidentally return the same random on startup so we
    // seed off secure random and then use random after that point.
    private static Random createRandom() {
        SecureRandom secureRandom = new SecureRandom();
        return new Random(secureRandom.nextLong());
    }

}
