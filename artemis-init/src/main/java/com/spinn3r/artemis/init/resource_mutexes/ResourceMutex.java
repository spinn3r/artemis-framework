package com.spinn3r.artemis.init.resource_mutexes;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 */
public class ResourceMutex implements Closeable {

    protected final File backing;

    public ResourceMutex(File backing) {
        this.backing = backing;
    }

    @Override
    public void close() throws IOException {
        Files.delete( backing.toPath() );
    }

}
