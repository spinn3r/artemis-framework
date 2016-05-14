package com.spinn3r.artemis.network.cookies.jar;

import com.google.common.net.HostAndPort;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps an index of cookies, by domain, so that we can re-use them again in the
 * future for additional requests.
 */
public class CookieJar {

    private final ConcurrentHashMap<HostAndPort,CookieHolderQueue> backing = new ConcurrentHashMap<>();

}
