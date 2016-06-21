package com.spinn3r.artemis.util.io;

import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class SocketsTest {

    @Test
    @Ignore
    public void testClosedPort() throws Exception {

        InetAddress localHost = InetAddress.getLocalHost();

        assertTrue(Sockets.isClosed(localHost, 9050) );

    }

    @Test
    public void testLocalhost() throws Exception {

        InetAddress localHost = InetAddress.getLocalHost();
        assertTrue(localHost.toString().endsWith("127.0.0.1"));

    }
}