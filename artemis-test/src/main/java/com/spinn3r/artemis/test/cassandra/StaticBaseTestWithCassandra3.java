package com.spinn3r.artemis.test.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.google.common.io.CharStreams;
import com.spinn3r.artemis.util.misc.Files;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class StaticBaseTestWithCassandra3 {

    protected static final String KEYSPACE = "blogindex";

    private static final int TIMEOUT = 4 * 60 * 1000;

    private static int transportPort;

    private static int storagePort;

    private static int rpcPort;

    private static String tmpdir;

    static {

        try {

            // initLogger();

            if ( "true".equals( System.getProperty( "cassandraunit.useDefaults" ) ) ) {

                System.setProperty( "cassandraunit.transportPort", "9142" );
                System.setProperty( "cassandraunit.storagePort", "7010" );
                System.setProperty( "cassandraunit.rpcPort", "9171" );
                System.setProperty( "cassandraunit.tmpdir", "/tmp/cassandraunit/default" );

            }

            requireSystemProperty( "cassandraunit.transportPort" );
            requireSystemProperty( "cassandraunit.storagePort" );
            requireSystemProperty( "cassandraunit.rpcPort" );
            requireSystemProperty( "cassandraunit.tmpdir" );

            // *** read configuration from system properties
            transportPort = Integer.parseInt( System.getProperty( "cassandraunit.transportPort" ) );
            storagePort = Integer.parseInt( System.getProperty( "cassandraunit.storagePort" ) );
            rpcPort = Integer.parseInt( System.getProperty( "cassandraunit.rpcPort" ) );
            tmpdir = System.getProperty( "cassandraunit.tmpdir" );

            System.out.printf( "Running embedded cassandraunit on transportPort=%s, storagePort=%s, rpcPort=%s, tmpdir=%s",
                               transportPort, storagePort, rpcPort, tmpdir );

            mktmpdir( tmpdir );

            System.setProperty( "cassandra.unsafetruncate", "true" );

            // *** write out a new config file.

            CassandraConfigWriter cassandraConfigWriter = new CassandraConfigWriter( transportPort, rpcPort, storagePort );
            File configFile = new File( tmpdir, "cassandra.yaml" );
            cassandraConfigWriter.write( configFile.getPath() );

            // *** now stat with this config file.
            EmbeddedCassandraServerHelper.startEmbeddedCassandra( configFile , tmpdir );
            initKeyspace();

        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

    }

    private static void requireSystemProperty( String name ) {

        if ( System.getProperty( name ) == null ) {
            throw new RuntimeException( String.format( "System property not defined: %s (consider adding -Dcassandraunit.useDefaults=true)", name ) );
        }

    }

    private static String mktmpdir(String tmpdir) throws IOException {
        File file = new File( tmpdir );

        if ( file.exists() ) {
            FileUtils.deleteRecursive( file );
        }

        if ( ! file.mkdirs() ) {
            throw new IOException( "Couldn't create tmpdir: " + tmpdir );
        }

        return tmpdir;
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
          .withPort( transportPort )
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
        DOMConfigurator.configure( StaticBaseTestWithCassandra3.class.getResource( "/log4j-stdout.xml" ) );
    }

}

