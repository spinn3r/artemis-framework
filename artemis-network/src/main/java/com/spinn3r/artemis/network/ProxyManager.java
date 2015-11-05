package com.spinn3r.artemis.network;


import com.spinn3r.log5j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:burton@tailrank.com">Kevin A. Burton (burtonator)</a>
 * @version $Id: RuntimeHandler.java,v 1.3 2005/03/07 20:39:11 burton Exp $
 */
public class ProxyManager {

    private static final Logger log = Logger.getLogger();

    /**
     * List of internal HTTP proxy servers we're using.
     */
    public List<String> proxies = new ArrayList<>();

    /**
     * List of URL regular expressions which are automatically proxied.
     */
    public List<String> patterns = new ArrayList<>();

    private static Iterator<String> iterator = null;

    /**
     *
     *
     */
    private boolean supported( String link ) {

        for( String proxy_pattern : patterns ) {

            Pattern p = Pattern.compile( proxy_pattern );
            Matcher m = p.matcher( link ) ;

            if ( m.find() ) {
                return true;
            }

        }

        return false;

    }

    /**
     * Get a proxy for us to use and rotate through them as they are being
     * accessed.
     *
     */
    public Proxy getProxy( String link ) throws NetworkException {

        if ( ! supported( link ) )
            return null;

        Proxy proxy = null;

        if ( proxies.size() > 0 ) {

            while( true ) {

                if ( iterator == null || iterator.hasNext() == false ) {
                    iterator = proxies.iterator();
                }

                String proxy_str = iterator.next();

                String host = proxy_str.split( ":" )[0];
                int port = Integer.parseInt( proxy_str.split( ":" )[1] );

                SocketAddress addr = new InetSocketAddress( host, port );

                Proxy.Type type = Proxy.Type.HTTP;

                proxy = new Proxy( type, addr );

                log.info( "Using proxy host %s:%s", host, port );

                break;

            }

        } else {
            throw new NetworkException( "No proxy configuration defined but URL proxy requested for: " + link );
        }

        return proxy;

    }

}

