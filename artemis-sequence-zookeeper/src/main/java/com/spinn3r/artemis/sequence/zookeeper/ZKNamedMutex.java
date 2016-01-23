package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.sequence.NamedMutex;
import com.spinn3r.artemis.sequence.NamedMutexException;
import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class ZKNamedMutex implements NamedMutex {

    private final CuratorFramework curatorClient;

    private final String path;

    private AtomicBoolean released = new AtomicBoolean( false );

    public ZKNamedMutex(CuratorFramework curatorClient, String path) {
        this.curatorClient = curatorClient;
        this.path = path;
    }

    @Override
    public void release() throws NamedMutexException {

        if ( released.get() ) {
            throw new NamedMutexException.AlreadyReleasedException( "Mutex is already released: " + path );
        }

        try {

            curatorClient.delete()
              .forPath( path )
            ;

            released.set( true );

        } catch (Exception e) {
            throw new NamedMutexException.FailureException( "Unable to delete named mutex: " + path, e );
        }

    }

}
