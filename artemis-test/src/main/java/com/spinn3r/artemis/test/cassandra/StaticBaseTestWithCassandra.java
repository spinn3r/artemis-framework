package com.spinn3r.artemis.test.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.spinn3r.artemis.util.misc.Files;
import org.apache.log4j.xml.DOMConfigurator;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class StaticBaseTestWithCassandra {

    protected static final String KEYSPACE = "blogindex";

    private static final int TIMEOUT = 4 * 60 * 1000;

    public static final int NATIVE_PORT = 9142;
    public static final int RPC_PORT = 9171;

    static {

        try {

            initLogger();

            System.setProperty( "cassandra.unsafetruncate", "true" );

            //EmbeddedCassandraServerHelper.startEmbeddedCassandra( "artemis-embedded-cassandra.yaml" );

            EmbeddedCassandraServerHelper.startEmbeddedCassandra();
            initKeyspace();


        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

    }

    private static void initKeyspace() throws InterruptedException {

        // try to connect and then create the keyspace.
        for (int i = 0; i < 30; i++) {

            try (Cluster cluster = newCluster();
                 Session session = cluster.connect();) {

                EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();

                session.execute( String.format( "create keyspace %s WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 } AND durable_writes = false;",
                                                KEYSPACE ) );

                break;

            } catch ( Exception e ) {
                System.out.printf( "." );
                Thread.sleep( 1000 );
            }

        }

    }

    protected static String getConfig() throws IOException {

        InputStream is = EmbeddedCassandraServerHelper.class.getResourceAsStream("/cu-cassandra.yaml");
        return Files.toUTF8( is );

    }

    protected static String getResource( Class clazz, String path ) throws IOException {

        try ( InputStream is =  clazz.getResourceAsStream( path ); ) {

            if ( is == null )
                throw new RuntimeException( "Resource not found: " + path );

            return CharStreams.toString( new InputStreamReader( is ) );

        }

    }

    protected static Cluster newCluster() {

        return Cluster.builder()
          .addContactPoint( "localhost" )
          .withPort( NATIVE_PORT )
          .withSocketOptions( new SocketOptions()
            .setConnectTimeoutMillis( TIMEOUT )
            .setReadTimeoutMillis( TIMEOUT ) )
          .build()
        ;

    }

    protected static void createTable( Class clazz, Session session, String table ) throws Exception {

        try {
            session.execute( String.format( "drop table if exists %s;", table ) );
        } catch ( Exception e ) {
            //
        }

        executeCQL( clazz, session, String.format( "/schema/cql/%s.cql", table ) );

    }

    protected static void executeCQL( Class clazz, Session session, String path ) throws IOException {

        String content = getResource( clazz, path );

        //String content = Files.toString( new File( path ), Charsets.UTF_8 );

        System.out.println( "==================" );
        System.out.printf( "Executing CQL: \n" );
        System.out.println( content );

        session.execute( content );

    }

    protected static void initLogger() {
        //DOMConfigurator.configure( StaticBaseTestWithCassandra.class.getResource( "/log4j-stdout.xml" ) );
    }

}
