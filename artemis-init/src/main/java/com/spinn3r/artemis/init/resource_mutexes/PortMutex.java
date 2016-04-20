package com.spinn3r.artemis.init.resource_mutexes;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class PortMutex extends ResourceMutex {

    private final int port;

    public PortMutex(File backing, int port) {
        super( backing );
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "PortMutex{" +
                 "port=" + port +
                 "} " + super.toString();
    }

    @Override
    public void close() throws IOException {
        // port mutex closing/releasing disabled for now
    }

}
