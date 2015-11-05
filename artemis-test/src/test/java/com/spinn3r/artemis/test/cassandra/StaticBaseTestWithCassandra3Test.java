package com.spinn3r.artemis.test.cassandra;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@Ignore
public class StaticBaseTestWithCassandra3Test {

    @Test
    public void test1() throws Exception {

        System.setProperty( "cassandraunit.transportPort", "9142" );
        System.setProperty( "cassandraunit.storagePort", "7010" );
        System.setProperty( "cassandraunit.rpcPort", "9171" );
        System.setProperty( "cassandraunit.tmpdir", "/tmp/cassandraunit1" );

        StaticBaseTestWithCassandra3.initLogger();

        System.out.printf( "%s\n", StaticBaseTestWithCassandra3.KEYSPACE );

        System.out.printf( "Cassandra is up!\n" );

        Thread.sleep( Long.MAX_VALUE );

    }

    @Test
    public void test2() throws Exception {

        System.setProperty( "cassandraunit.transportPort", "9143" );
        System.setProperty( "cassandraunit.storagePort", "7011" );
        System.setProperty( "cassandraunit.rpcPort", "9172" );
        System.setProperty( "cassandraunit.tmpdir", "/tmp/cassandraunit2" );

        StaticBaseTestWithCassandra3.initLogger();

        System.out.printf( "%s\n", StaticBaseTestWithCassandra3.KEYSPACE );

        System.out.printf( "Cassandra is up!\n" );

        Thread.sleep( Long.MAX_VALUE );

    }


}