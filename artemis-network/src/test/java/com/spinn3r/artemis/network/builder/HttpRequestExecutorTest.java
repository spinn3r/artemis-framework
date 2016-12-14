package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.URLResourceRequest;
import org.junit.Test;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import static com.spinn3r.artemis.network.builder.HttpRequest.*;
import static com.spinn3r.artemis.network.builder.HttpRequestExecutor.isTransientHttpException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class HttpRequestExecutorTest {

    @Test
    public void testTransientExceptions() {

        assertTrue(isTransientHttpException(new NetworkException("", 503)));
        assertTrue(isTransientHttpException(new NetworkException("", STATUS_CONNECT_TIMEOUT)));
        assertTrue(isTransientHttpException(new NetworkException("", STATUS_READ_TIMEOUT)));
        assertTrue(isTransientHttpException(new SSLException("")));
        assertTrue(isTransientHttpException(new RuntimeException(new NetworkException("", 503))));
        assertTrue(isTransientHttpException(new SocketTimeoutException("")));
        assertTrue(isTransientHttpException(new SocketException("")));
        assertTrue(isTransientHttpException(new NetworkException("", new SocketTimeoutException(""))));

    }

    @Test
    public void testWithTunnelFailure() throws Exception {

        assertTrue(isTransientHttpException(new IOException("Unable to tunnel through proxy. Proxy returns \"HTTP/1.0 503 Service Unavailable\"")));
        assertTrue(isTransientHttpException(new NetworkException("", new IOException("Unable to tunnel through proxy. Proxy returns \"HTTP/1.0 503 Service Unavailable\""))));

    }

    @Test
    public void testFatalNetworkExceptions() throws Exception {
        assertFalse(isTransientHttpException(new NetworkException("", 404)));
        assertFalse(isTransientHttpException(new RuntimeException()));
        assertFalse(isTransientHttpException(new RuntimeException(new NetworkException("", 404))));
    }

    @Test
    public void testSSLHandshakeException() throws Exception {

        assertTrue(isTransientHttpException(new SSLHandshakeException("Remote host closed connection during handshake")));

    }
}

