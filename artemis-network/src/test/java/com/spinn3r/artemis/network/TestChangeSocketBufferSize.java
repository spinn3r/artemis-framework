package com.spinn3r.artemis.network;

import org.junit.Test;

import java.net.Socket;

/**
 *
 */
public class TestChangeSocketBufferSize {

    @Test
    public void test1() throws Exception {

        // we should be able to poke a hole in the URL API to set these options
        // directly
        Socket socket = new Socket( "cnn.com", 80 );

        //socket.setSendBufferSize(  );
        //socket.setReceiveBufferSize(  );

    }


}
