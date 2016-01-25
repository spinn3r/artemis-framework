package com.spinn3r.artemis.zookeeper.embedded;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.resource_mutexes.PortMutex;
import com.spinn3r.artemis.init.resource_mutexes.PortMutexes;
import com.spinn3r.artemis.util.io.Sockets;
import com.spinn3r.artemis.zookeeper.init.ZookeeperConfig;
import org.apache.curator.test.TestingServer;

import java.net.InetAddress;

/**
 *
 */
public class EmbeddedZookeeperService extends BaseService {

    private final PortMutexes portMutexes;

    private final AtomicReferenceProvider<EmbeddedZookeeperPort> embeddedZookeeperPortProvider
      = new AtomicReferenceProvider<>( null );

    protected TestingServer testingServer;

    private EmbeddedZookeeperPort embeddedZookeeperPort = null;

    private PortMutex portMutex = null;

    @Inject
    EmbeddedZookeeperService(PortMutexes portMutexes) {

        this.portMutexes = portMutexes;
    }

    @Override
    public void init() {
        provider( EmbeddedZookeeperPort.class, embeddedZookeeperPortProvider );
    }

    @Override
    public void start() throws Exception {

        this.portMutex = portMutexes.acquire( 64149, 64300 );
        this.embeddedZookeeperPort = new EmbeddedZookeeperPort( this.portMutex.getPort() );
        this.embeddedZookeeperPortProvider.set( embeddedZookeeperPort );

        testingServer = new TestingServer( this.portMutex.getPort() );

        info( "Running ZK service on port: %s\n", this.portMutex.getPort() );

        // now advertise a ZookeeperConfig that other services can use.

        ZookeeperConfig zookeeperConfig = new ZookeeperConfig( Lists.newArrayList( "localhost:" + portMutex.getPort() ) );
        advertise( ZookeeperConfig.class, zookeeperConfig );

    }

    @Override
    public void stop() throws Exception {

        if ( testingServer != null ) {
            testingServer.stop();
        }

        if ( embeddedZookeeperPort != null ) {
            Sockets.waitForClosedPort( InetAddress.getByName( "localhost" ), embeddedZookeeperPort.getPort() );
        }

        if( portMutex != null ) {
            portMutex.close();
        }

    }

}
