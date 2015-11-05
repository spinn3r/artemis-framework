package com.spinn3r.artemis.test.zookeeper;

import com.spinn3r.artemis.util.io.Sockets;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;

import java.net.InetAddress;

/**
 *
 */
public class BaseZookeeperTest {

    // we have to use a known port instead of using a different one each time.
    private static final int PORT = 64148;

    protected TestingServer testingServer;

    @Before
    public void setUp() throws Exception {

        testingServer = new TestingServer( PORT );

        System.out.printf( "Running ZK service on port: %s\n", testingServer.getPort() );

    }

    @After
    public void tearDown() throws Exception {
        testingServer.stop();
        Sockets.waitForClosedPort( InetAddress.getByName( "localhost" ), PORT );
    }

    protected String getZookeeperConnectString() {
        return String.format( "localhost:%s", testingServer.getPort() );
    }

}

