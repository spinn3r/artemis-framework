package com.spinn3r.artemis.network;

import org.junit.Assert;
import org.junit.Test;

public class NetworkExceptionTest {

    @Test
    public void testSimpleNE() {

        String message = "Message";
        NetworkException networkException = new NetworkException(message);

        Assert.assertEquals(message, networkException.getLocalizedMessage());
        Assert.assertEquals(message, networkException.getMessage());
        Assert.assertEquals(null, networkException.getURL());
        Assert.assertEquals(-1, networkException.getResponseCode());
        Assert.assertFalse(networkException.isProxyError());
        Assert.assertFalse(networkException.isTransient());
        Assert.assertEquals(null, networkException.status);
        Assert.assertEquals(null, networkException.e);
        Assert.assertEquals(null, networkException.getCause());
    }

    @Test
    public void testMessageAndResponseCode() {
        String message = "Message";
        int responseCodeOk = 200;
        NetworkException networkException = new NetworkException(message, responseCodeOk);

        Assert.assertEquals(message, networkException.getLocalizedMessage());
        Assert.assertEquals(message, networkException.getMessage());
        Assert.assertEquals(null, networkException.getURL());
        Assert.assertEquals(responseCodeOk, networkException.getResponseCode());
        Assert.assertFalse(networkException.isProxyError());
        Assert.assertFalse(networkException.isTransient());
        Assert.assertEquals(null, networkException.status);
        Assert.assertEquals(null, networkException.e);
        Assert.assertEquals(null, networkException.getCause());
    }

    @Test
    public void testSimpleNestedExceptionAndMessage() {
        String message = "Message";
        String innerMsg = "Inner MSG";
        Throwable testThrowable = new Throwable(innerMsg);
        NetworkException networkException = new NetworkException(message, testThrowable);

        Assert.assertEquals(message + ": java.lang.Throwable: Inner MSG", networkException.getLocalizedMessage());
        Assert.assertEquals(message + ": java.lang.Throwable: Inner MSG", networkException.getMessage());
        Assert.assertEquals(null, networkException.getURL());
        Assert.assertEquals(-1, networkException.getResponseCode());
        Assert.assertFalse(networkException.isProxyError());
        Assert.assertFalse(networkException.isTransient());
        Assert.assertEquals(null, networkException.status);

        Assert.assertEquals(testThrowable, networkException.getCause());
        Assert.assertEquals(null, networkException.e); //Is this a bug ? maybe the inner exception should be removed in favour of Exception#getCause
    }

    @Test
    public void testSimpleNestedException() {

        String innerMsg = "Inner MSG";
        Throwable testThrowable = new Throwable(innerMsg);
        NetworkException networkException = new NetworkException(testThrowable);

        Assert.assertEquals("java.lang.Throwable: Inner MSG", networkException.getLocalizedMessage());
        Assert.assertEquals("java.lang.Throwable: Inner MSG", networkException.getMessage());
        Assert.assertEquals(null, networkException.getURL());
        Assert.assertEquals(-1, networkException.getResponseCode());
        Assert.assertFalse(networkException.isProxyError());
        Assert.assertFalse(networkException.isTransient());
        Assert.assertEquals(null, networkException.status);

        Assert.assertEquals(testThrowable, networkException.getCause());
        Assert.assertEquals(null, networkException.e); //Is this a bug ? maybe the inner exception should be removed in favour of Exception#getCause
    }

}