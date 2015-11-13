package com.spinn3r.artemis.util.io;

import com.google.common.util.concurrent.Uninterruptibles;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Sockets {


    public static void waitForClosedPort( InetAddress inetAddress, int port) {
        waitForClosedPort( inetAddress, port, 30000 );
    }

    public static void waitForClosedPort( InetAddress inetAddress, int port, long timeout ) {

        System.out.printf( "Waiting for port %s on %s to close ...", inetAddress, port );

        long before = System.currentTimeMillis();

        while( isOpen( inetAddress, port ) ) {
            Uninterruptibles.sleepUninterruptibly( 1000, TimeUnit.MILLISECONDS );
            System.out.printf( "." );

            long now = System.currentTimeMillis();

            if ( now - before > timeout ) {
                throw new RuntimeException( String.format( "Could not close port %s within %s ms on %s", port, timeout, inetAddress ) );
            }

        }

        System.out.printf( "done\n" );

    }

    public static boolean isOpen( InetAddress inetAddress, int port) {

        try {

            try( Socket socket = new Socket( inetAddress, port ) ) {
                socket.getInputStream();
                return true;
            }

        } catch (IOException e) {
            return false;
        }

    }

    public static void waitForOpenPort(String host, int port) {

        Throwable cause = null;

        System.out.printf( "Waiting to connect to %s:%s ...", host, port );

        for (int i = 0; i < 15; i++) {

            try ( Socket socket = new Socket( host, port ); ) {

                if (socket.isConnected()) {
                    System.out.printf( " OPEN\n" );
                    return;
                }

            } catch( IOException e ) {
                cause = e;
            }

            System.out.printf( "." );
            Uninterruptibles.sleepUninterruptibly( 1_000, TimeUnit.MILLISECONDS );

        }

        throw new RuntimeException( cause );

    }

}
