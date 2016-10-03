package com.spinn3r.artemis.network;

/**
 *
 */
public interface NetworkFailure {

    boolean isProxyError();

    int getResponseCode();

}
