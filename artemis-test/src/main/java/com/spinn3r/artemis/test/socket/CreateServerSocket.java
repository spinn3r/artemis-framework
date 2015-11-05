package com.spinn3r.artemis.test.socket;

import com.spinn3r.artemis.util.misc.Getopt;

import java.net.ServerSocket;

/**
 * Test creating a server socket and implement some specific behavior.
 */
public class CreateServerSocket {

    public static void main(String[] args) throws Exception {

        Getopt getopt = new Getopt( args );

        String exit = getopt.getString( "exit", "clean" );
        int port = getopt.getInt( "port", 12121 );

        System.out.printf( "Going to exit as %s and bind port %s\n", exit, port );
        ServerSocket serverSocket = new ServerSocket( port );

        switch (exit) {

            case "clean":
                System.out.printf( "Closing port...\n" );
                serverSocket.close();
                System.out.printf( "Closing port...done\n" );
                break;

            case "ugly":
                System.out.printf( "Exiting without closing port.\n" );
                System.exit( 1 );
                break;

        }

    }

}
