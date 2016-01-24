package com.spinn3r.artemis.http.init;

import com.google.inject.Singleton;

/**
 * Defines the port that the webserver is running on so that we can inject it
 * as a dependency to read the port used in tests.
 */
@Singleton
public class WebserverPort {

    private final int port;

    public WebserverPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "WebserverPort{" +
                 "port=" + port +
                 '}';
    }

}
