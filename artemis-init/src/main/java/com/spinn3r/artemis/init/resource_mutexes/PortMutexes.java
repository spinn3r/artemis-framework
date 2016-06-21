package com.spinn3r.artemis.init.resource_mutexes;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.io.Sockets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Acquire a mutex based on TCP port numbers.
 */
public class PortMutexes {

    /**
     * Acquire a mutex for a port in the given range.
     *
     * @param startPort The starting port (inclusive)
     * @param endPort The ending port (inclusive)
     */
    public PortMutex acquire( int startPort, int endPort ) throws ResourceMutexException {

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

                if ( isClosed(port) && ! portFile.exists() ) {

                    Optional<FileLock> fileLock = acquireFileLock(portFile);

                    if ( fileLock.isPresent() ) {
                        return new PortMutex(portFile, fileLock.get(), port);
                    }

                }

            }

            String msg = String.format( "Unable to allocate port mutex between range %s and %s",
                                        startPort, endPort );

            throw new ResourceMutexException.ExhaustedException( msg );

        } catch (IOException e) {
            throw new ResourceMutexException.FailureException( e );
        }

    }

    private boolean isClosed( int port ) throws UnknownHostException {

        if( Sockets.isOpen( InetAddress.getLocalHost(), port ) ) {
            return false;
        }

        if ( Sockets.isOpen( InetAddress.getByName("127.0.0.1"), port) ) {
            return false;
        }

        return true;

    }

    private Optional<FileLock> acquireFileLock(File file) {

        try {

            //Path path = file.toPath();
            //FileChannel fileChannel = FileChannel.open(path, CREATE_NEW, DELETE_ON_CLOSE, WRITE, APPEND);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileOutputStream.getChannel();

            fileChannel.force(true);

            FileLock fileLock = fileChannel.lock();

            return Optional.of(fileLock);

        } catch (IOException e) {
            return Optional.empty();
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
