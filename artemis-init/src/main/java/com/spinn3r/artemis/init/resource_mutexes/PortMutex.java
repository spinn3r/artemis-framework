package com.spinn3r.artemis.init.resource_mutexes;

import java.io.File;
import java.nio.channels.FileLock;

/**
 *
 */
public class PortMutex extends ResourceMutex {

    private final int port;

    public PortMutex(File backing, FileLock fileLock, int port) {
        super(backing, fileLock);
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

}
