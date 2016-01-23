package com.spinn3r.artemis.sequence.zookeeper;

import com.google.common.base.Preconditions;
import com.spinn3r.artemis.sequence.*;
import com.spinn3r.log5j.Logger;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

/**
 * Factory to create global mutexes, keep track of their state, and expire them
 * if necessary.
 */
public class ZKNamedMutexFactory implements NamedMutexFactory, AutoCloseable {

    private static final Logger log = Logger.getLogger();

    private static final String BASEDIR = "/mutexes/named";

    private String connectString;

    private String namespace;

    private CuratorFramework curatorClient;

    // I don't think we can retry with ephemeral nodes as the nodes will be gone.
    private RetryPolicy retryPolicy = new RetryNTimes( 0, 0 );

    public ZKNamedMutexFactory(String connectString,
                               int connectionTimeout,
                               int sessionTimeout,
                               String namespace )
      throws GlobalMutexAcquireException {

        this.connectString = connectString;
        this.namespace = namespace;

        try {

            curatorClient = CuratorFrameworkFactory
                                .builder()
                                .connectString( connectString )
                                .retryPolicy( retryPolicy )
                                .connectionTimeoutMs( connectionTimeout )
                                .sessionTimeoutMs( sessionTimeout )
                                .namespace( namespace )
                                .build();

              curatorClient.start();

            if ( curatorClient.checkExists().forPath( BASEDIR ) == null ) {
                curatorClient.create().forPath( BASEDIR );
            }

        } catch (Exception e) {
            throw new GlobalMutexAcquireException( "Unable to : ", e );
        }

    }

    @Override
    public NamedMutex acquire( String name ) throws NamedMutexException {

        Preconditions.checkNotNull( name );

        try {

            String potentialPath = BASEDIR + "/" + name;

            curatorClient.create()
               .withMode( CreateMode.EPHEMERAL )
               .forPath( potentialPath )
               ;

            log.info( "Acquired named mutex: %s" , name );

            return new ZKNamedMutex( curatorClient, potentialPath );

        } catch ( KeeperException.NodeExistsException e ) {

            log.info( "Failed to acquire named mutex: %s" , name );
            throw new NamedMutexException.AcquireException( "Unable to acquire mutex: " + name );

        } catch ( Exception e ) {
            throw new NamedMutexException.FailureException( "Unable to acquire mutex: " + name, e );
        }

    }

    protected CuratorFramework getCuratorClient() {
        return curatorClient;
    }

    @Override
    public void close() throws Exception {
        this.curatorClient.close();
    }

}
