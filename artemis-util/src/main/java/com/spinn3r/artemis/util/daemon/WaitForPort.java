package com.spinn3r.artemis.util.daemon;

import com.spinn3r.artemis.util.misc.Getopt;

import java.io.IOException;
import java.net.*;

/**
 * Wait for a given TCP port to open... or the timeout, whichever comes first.
 */
public class WaitForPort {

    private static final int TIMEOUT = 60000;

    private boolean tracingEnabled = false;

    /**
     * When true we print the status to stdout.
     * @param tracingEnabled
     */
    public void setTracingEnabled(boolean tracingEnabled) {
        this.tracingEnabled = tracingEnabled;
    }

    public void waitFor( int port, int timeout ) throws Exception {
        waitFor( InetAddress.getLocalHost(), port, timeout );
    }

    public void waitFor( String host, int port, int timeout ) throws Exception {
        waitFor( InetAddress.getByName( host ), port, timeout );
    }

    public void waitFor( InetAddress inetAddress, int port, int timeout ) throws Exception {

        if ( "true".equals( System.getProperty( "waitforport.disabled" ) ) ) {
            return;
        }

        trace( "Waiting for port %s on %s (timeout: %,d ms)\n", port, inetAddress, timeout );

        long now = System.currentTimeMillis();

        SocketAddress addr = new InetSocketAddress( inetAddress, port );

        while( true ) {

            Socket sock = new Socket();

            try {

                sock.connect( addr, 5000 );
                //success ... we are up so just exit.

                trace( "\n" );

                trace( "SUCCESS: daemon up and listening on port %s\n", port );
                return;

            } catch ( SocketTimeoutException|ConnectException e ) {

                // this is a normal timeout.  We are expecting this to happen.

            } catch ( IOException e ) {
                e.printStackTrace();
            } finally {
                sock.close();
            }

            trace( "." );

            Thread.sleep( 1000L );

            if ( System.currentTimeMillis() > now + timeout ) {

                String msg = String.format( "Exceeded timeout %,d ms waiting port %s at sock address %s",
                                            timeout, port, addr );

                throw new Exception( msg );

            }

        }

    }

    private void trace(String msg, Object... args) {
        if ( tracingEnabled ) {
            System.out.printf( msg, args );
        }
    }

    public static void main( String[] args ) throws Exception {

        Getopt getopt = new Getopt( args );

        getopt.require( "port" );

        int port = getopt.getInt( "port" );

        int timeout = getopt.getInt( "timeout", TIMEOUT );

        String host = getopt.getString( "host", "localhost" );

        WaitForPort waitForPort = new WaitForPort();
        waitForPort.setTracingEnabled( true );

        waitForPort.waitFor( host, port, timeout );

    }

}
