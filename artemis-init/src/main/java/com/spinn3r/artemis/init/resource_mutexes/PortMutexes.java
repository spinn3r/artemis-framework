package com.spinn3r.artemis.init.resource_mutexes;

import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import com.spinn3r.artemis.util.io.Sockets;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

/**
 * Acquire a mutex based on TCP port numbers.
 */
public class PortMutexes {

    public PortMutex acquire( int startPort, int endPort ) throws ResourceMutexException {

        try {
            return acquire( InetAddress.getLocalHost(), startPort, endPort );
        } catch (UnknownHostException e) {
            throw new ResourceMutexException.FailureException( e );
        }

    }

    /**
     * Acquire a mutex for a port in the given range.
     *
     * @param startPort The starting port (inclusive)
     * @param endPort The ending port (inclusive)
     */
    public PortMutex acquire( InetAddress inetAddress, int startPort, int endPort ) throws ResourceMutexException {

        try {

            requireDir( "/tmp/named-mutexes" );
            requireDir( "/tmp/named-mutexes/ports" );

            File parent = new File( "/tmp/named-mutexes/ports" );

            // create the list of ports that we can use.

            List<Integer> ports = Lists.newArrayList();
            for (int port = startPort; port <= endPort; port++) {
                ports.add( port );
            }

            // randomize the ports so that tests that might be accidentally
            // hard code to the first port fail immediately.
            Collections.shuffle( ports );

            for (int port : ports) {

                File portFile = new File( parent, Integer.toString( port ) );

                if ( Sockets.isClosed( inetAddress, port  ) &&
                     ! portFile.exists() &&
                     portFile.createNewFile() ) {

                    return new PortMutex( portFile, port );

                }

            }

            String msg = String.format( "Unable to allocate port mutex between range %s and %s",
                                        startPort, endPort );

            throw new ResourceMutexException.ExhaustedException( msg );

        } catch (IOException e) {
            throw new ResourceMutexException.FailureException( e );
        }

    }

    private File requireDir( String path ) throws IOException {

        File dir = new File( path );

        if( ! dir.exists() ) {

            try {

                Files.createDirectory( dir.toPath() );

            } catch (FileAlreadyExistsException e) {
                // this is acceptable as this was caused by a race.
            }

        }

        return dir;

    }

}
