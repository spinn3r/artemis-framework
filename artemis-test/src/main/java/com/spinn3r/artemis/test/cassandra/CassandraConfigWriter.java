package com.spinn3r.artemis.test.cassandra;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.util.misc.Files;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

/**
 * Read the stock config and then write a new config with new ports.
 */
public class CassandraConfigWriter {

    private final int transportPort;

    private final int rpcPort;

    private final int storagePort;

    public CassandraConfigWriter(int transportPort, int rpcPort, int storagePort) {
        this.transportPort = transportPort;
        this.rpcPort = rpcPort;
        this.storagePort = storagePort;
    }

    protected String readConfig() throws IOException {

        InputStream is = EmbeddedCassandraServerHelper.class.getResourceAsStream("/cu-cassandra.yaml");
        return Files.toUTF8( is );

    }

    protected String updateConfig( String config ) {

        config = config.replaceAll( "native_transport_port: [0-9]+", "native_transport_port: " + transportPort );
        config = config.replaceAll( "rpc_port: [0-9]+", "rpc_port: " + rpcPort );
        config = config.replaceAll( "storage_port: [0-9]+", "storage_port: " + storagePort );

        return config;

    }

    public void write( String path ) throws IOException {

        String config = readConfig();
        config = updateConfig( config );

        try( FileOutputStream fos = new FileOutputStream( path ) ) {
            fos.write( config.getBytes( Charsets.UTF_8 ) );
        }

    }

}
