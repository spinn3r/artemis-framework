package com.spinn3r.artemis.test.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.spinn3r.artemis.http.servlets.BaseServlet;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import org.apache.cassandra.service.CassandraDaemon;
import org.apache.log4j.xml.DOMConfigurator;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 *
 */
@Config( path = "embedded-cassandra.conf",
         required = false,
         implementation = EmbeddedCassandraConfig.class )

public class EmbeddedCassandraServices extends BaseService {

    private final EmbeddedCassandraConfig config;

    @Inject
    public EmbeddedCassandraServices(EmbeddedCassandraConfig config) {
        this.config = config;
    }

    @Override
    public void start() throws Exception {

        EmbeddedCassandraServerHelper.startEmbeddedCassandra();

        // try to connect first

        try (Cluster cluster = newCluster( config );
             Session session = cluster.connect();) {

            EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();

            session.execute( String.format( "create keyspace %s WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };",
                                            config.getKeyspace() ) );

        }


    }

    @Override
    public void stop() throws Exception {

        // NOTE: in the past we stopped cassandra unit but it doesn't actually
        // work so the method was deprecated and stop() in cassandra unit was
        // a noop.

        Field field = EmbeddedCassandraServerHelper.class.getDeclaredField( "cassandraDaemon" );

        field.setAccessible( true );

        CassandraDaemon daemon = (CassandraDaemon)field.get( null );

        daemon.stop();

        System.out.printf( "FIXME: sleeping...\n" );
        Thread.sleep( 15000 );

    }

    protected static Cluster newCluster( EmbeddedCassandraConfig config ) {

        return Cluster.builder()
          .addContactPoint( "localhost" )
          .withPort( config.getPort() )
          .withSocketOptions( new SocketOptions()
                                .setConnectTimeoutMillis( config.getTimeout() )
                                .setReadTimeoutMillis( config.getTimeout() ) )
          .build()
        ;

    }


}
