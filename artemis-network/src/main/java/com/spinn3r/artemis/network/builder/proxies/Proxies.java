package com.spinn3r.artemis.network.builder.proxies;

import com.spinn3r.artemis.network.builder.HttpRequestBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Proxies {

    public static ProxyReference create( String type, String host, int port ) {

        checkNotNull( type );

        Proxy.Type proxyType;

        switch( type.toUpperCase() ) {

            case "HTTP":
                proxyType = Proxy.Type.HTTP;
                break;

            default:
                throw new RuntimeException( "Invalid proxy type: " + type );

        }

        SocketAddress addr = new InetSocketAddress( host, port );

        Proxy proxy = new Proxy( proxyType, addr );
        return new ProxyReference( host, port, proxy );

    }

    public static ProxyReference create( String proxy ) {

        Pattern pattern = Pattern.compile( "(http)://([^:]+):([0-9]+)" );

        Matcher matcher = pattern.matcher( proxy );

        if ( matcher.find() ) {

            String type = matcher.group( 1 );
            String host = matcher.group( 2 );
            int port = Integer.parseInt( matcher.group( 3 ) );

            return create( type, host, port );

        } else {
            throw new RuntimeException( "Unable to set proxy for: " + proxy );
        }

    }

    public static String format( Proxy proxy ) {
        return String.format( "%s:/%s", proxy.type().toString().toLowerCase(), proxy.address() );
    }

}
