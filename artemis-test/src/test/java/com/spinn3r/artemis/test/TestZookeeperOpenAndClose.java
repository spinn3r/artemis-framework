package com.spinn3r.artemis.test;

import com.spinn3r.artemis.util.io.Sockets;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;

/**
 *
 */
@Ignore
public class TestZookeeperOpenAndClose {

    // we have to use a known port instead of using a different one each time.
    private static final int PORT = 64148;


    @Test
    public void test1() throws Exception {

        for (int i = 0; i < 10; i++) {

            TestingServer testingServer = new TestingServer( PORT );
            testingServer.stop();

        }

    }

}
