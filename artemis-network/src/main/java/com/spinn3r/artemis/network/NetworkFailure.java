package com.spinn3r.artemis.network;

/**
 *
 */
public interface NetworkFailure {

    String getResource();

    boolean isProxyError();

    int getResponseCode();

}
