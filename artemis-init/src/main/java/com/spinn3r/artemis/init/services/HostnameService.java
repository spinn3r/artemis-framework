package com.spinn3r.artemis.init.services;

import com.google.inject.Provider;
import com.spinn3r.artemis.fluent.Tuples;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.util.misc.Files;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class HostnameService extends BaseService implements Provider<Hostname>, HostnameServiceType, ModularService {

    private Hostname hostname;

    @Override
    public void init() {
        provider( Hostname.class, this );
    }

    @Override
    protected void configure() {
        bind( Hostname.class ).toProvider( this );
    }

    @Override
    public void start() throws Exception {

        this.hostname = new Hostname( readHostname() );

        info( "Using hostname: %s", hostname.getValue() );

    }

    @Override
    public Hostname get() {
        return hostname;
    }

    /**
     * Read the current hostname from /etc/hostname.
     *
     * @return
     * @throws java.io.IOException
     */
    public static String readHostname() throws IOException {

        String result =
          Tuples.strs( Tuples.str( System.getenv( "HOSTNAME" ) ),
                       Tuples.str( read( "/etc/artemis/hostname" ) ).find( "." ),
                       Tuples.str( read( "/etc/hostname" ) ).find( "." ),
                       Tuples.str( read( "/proc/sys/kernel/hostname" ) ).find( "." ) )
            .first();

        if ( result == null )
            throw new IOException( "Unable to read hostname. " );

        return result;

    }

    private static String read( String path ) throws IOException {

        File file = new File( path );

        if ( ! file.exists() )
            return null;

        String data = Files.toUTF8( file );

        if ( data != null )
            return data.trim();

        return null;

    }

}
