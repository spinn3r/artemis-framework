package com.spinn3r.artemis.test.cassandra;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CassandraConfigWriterTest {

    @Test
    public void test() throws Exception {

        CassandraConfigWriter cassandraConfigWriter = new CassandraConfigWriter( 123, 456, 789 );

        String config = cassandraConfigWriter.readConfig();
        assertTrue( config.contains( "native_transport_port: 9142" ) );
        assertTrue( config.contains( "rpc_port: 9171" ) );
        assertTrue( config.contains( "storage_port: 7010" ) );

        config = cassandraConfigWriter.updateConfig(config);

        assertTrue( config.contains( "native_transport_port: 123" ) );
        assertTrue( config.contains( "rpc_port: 456" ) );
        assertTrue( config.contains( "storage_port: 789" ) );

        System.out.printf( "%s\n", config );

    }

}