package com.spinn3r.artemis.init.resource_mutexes;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class ResourceMutex implements Closeable {

    protected final File backing;

    protected final FileLock fileLock;

    protected final AtomicBoolean closed = new AtomicBoolean( false );

    public ResourceMutex(File backing, FileLock fileLock) {
        this.backing = backing;
        this.fileLock = fileLock;
    }

    @Override
    public void close() throws IOException {

        if ( this.closed.get() )
            return;

        fileLock.release();
        fileLock.channel().close();

        Files.delete( backing.toPath() );
        this.closed.set( true );

    }

}
