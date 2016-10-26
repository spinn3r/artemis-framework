package com.spinn3r.artemis.util.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Comparator;

/**
 * Represents a host and port.
 */
public class HostPort implements Comparable<HostPort> {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostPort)) return false;

        HostPort hostPort = (HostPort) o;

        if (port != hostPort.port) return false;
        return hostname.equals(hostPort.hostname);

    }

    @Override
    public int compareTo(HostPort o) {
        return format().compareTo(o.format());
    }

    @Override
    public int hashCode() {
        int result = hostname.hashCode();
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "HostPort{" +
                 "hostname='" + hostname + '\'' +
                 ", port=" + port +
                 '}';
    }

}
