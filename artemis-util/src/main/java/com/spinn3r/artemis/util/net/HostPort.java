package com.spinn3r.artemis.util.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Represents a host and port.
 */
public class HostPort {

    private final String hostname;

    private final int port;

    /**
     * Create from hostname:port syntax
     */
    public HostPort( String hostnameWithPort ) {

        String[] split = hostnameWithPort.split( ":" );

        if ( split.length != 2 ) {
            throw new IllegalArgumentException( "Must be host:port syntax: " + hostnameWithPort );
        }

        this.hostname = split[0];
        this.port = Integer.parseInt( split[1] );

    }

    public HostPort(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public InetSocketAddress toInetSocketAddress() throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getByName( getHostname() );
        return new InetSocketAddress( inetAddress, getPort() );

    }

    public String format() {
        return String.format( "%s:%s", hostname, port );
    }

    @Override
    public String toString() {
        return "HostPort{" +
                 "hostname='" + hostname + '\'' +
                 ", port=" + port +
                 '}';
    }

}
