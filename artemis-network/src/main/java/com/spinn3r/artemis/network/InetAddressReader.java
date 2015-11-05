package com.spinn3r.artemis.network;

import sun.net.www.http.HttpClient;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLConnection;

/**
 * Takes a URL connections and then results the IP address used to fetch the
 * data.
 */
public class InetAddressReader {

    public static InetAddress read( URLConnection conn ) throws Exception {

        HttpURLConnection httpURLConnection = (HttpURLConnection)conn;

        // TODO: this code isn't very readable and could probably be improved
        // by a fluent API

        Socket serverSocket = null;

        if ( httpURLConnection instanceof HttpsURLConnectionImpl ) {

            Object delegate = readDeclaredField( httpURLConnection, "delegate" );

            Object http = readDeclaredField( delegate, sun.net.www.protocol.http.HttpURLConnection.class, "http" );

            if ( http != null ) {
                serverSocket = (Socket) readDeclaredField( http, sun.net.NetworkClient.class, "serverSocket" );
            }

        } else {

            //conn.http.serverSocket
            // serverSocket...

            HttpClient http = (HttpClient) readDeclaredField( httpURLConnection, sun.net.www.protocol.http.HttpURLConnection.class, "http" );

            if ( http != null ) {
                serverSocket = (Socket) readDeclaredField( http, sun.net.NetworkClient.class, "serverSocket" );
            }

        }

        if ( serverSocket == null )
            return null;

        return serverSocket.getInetAddress();


    }

    private static Object readDeclaredField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return readDeclaredField( obj, obj.getClass(), fieldName );
    }

    private static Object readDeclaredField(Object obj, Class clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {

        Field field = clazz.getDeclaredField( fieldName );
        field.setAccessible( true );

        return field.get( obj );

    }

    public static void introspectViaReflection(Object obj) {

        Class clazz = obj.getClass();

        while( true ) {

            if ( clazz == null )
                break;

            introspectViaReflection( obj, clazz );

            clazz = clazz.getSuperclass();

        }

    }

    public static void introspectViaReflection(Object obj, Class clazz) {

        Field[] fields;

        System.out.printf( "============== declared fields in %s\n", clazz.getName() );

        fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[ i ];
            System.out.printf( "  declared field: %s\n", field );
        }

        System.out.printf( "============== fields in %s\n", clazz.getName() );

        fields = clazz.getFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[ i ];
            System.out.printf( "  field: %s\n", field );
        }

    }


}
