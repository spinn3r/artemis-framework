package com.spinn3r.artemis.test.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Collection;
import java.util.Set;

/**
 *
 */
public class BaseTestWithCassandra extends StaticBaseTestWithCassandra3 {

    protected static Cluster cluster;

    protected static Session session;

    protected static Provider<Session> sessionProvider;

    @Before
    public void setUp() throws Exception {

        // try to load our schema...
        createTable( "source" );

        createTable( "content" );
        createTable( "robot_link_filter" );
        createTable( "robot_flat_link_filter" );

        //createTable( "content_idx_resource" );
        //createTable( "content_idx_source_hashcode" );
        createTable( "user" );
        createTable( "vendor" );
        createTable( "discovery" );
        createTable( "twitter_graph" );
        createTable( "twitter_link" );
        createTable( "twitter_link_meta" );

    }

    @After
    public void tearDown() throws Exception {

        KeyspaceMetadata keyspaceMetadata =
          session.getCluster().getMetadata().getKeyspace( KEYSPACE );

        if ( keyspaceMetadata == null )
            throw new RuntimeException( "No keyspace named: " + KEYSPACE );

        Collection<TableMetadata> tables = keyspaceMetadata.getTables();

        System.out.printf( "Dropping tables to cleanup during tearDown: \n" );

        for (TableMetadata table : tables) {
            System.out.printf( "Dropping table: %s\n", table.getName() );
            dropTable( table.getName() );
        }

    }

    static {

        try {

            cluster = newCluster();

            session = cluster.connect( KEYSPACE );

            sessionProvider = new AtomicReferenceProvider<>( session );

        } catch (Exception e) {
            throw new RuntimeException( e );
        }

    }

    protected static void createTable( String table ) throws Exception {
        createTable( BaseTestWithCassandra.class, session, table );
    }

    protected static void dropTable( String table ) throws Exception {

        session.execute( String.format( "drop table if exists %s;", table ) );

    }

    public static class CassandraSessionProviderService extends BaseService implements Provider<Session> {

        @Override
        public void init() {
            provider( Session.class, this );
        }

        @Override
        public Session get() {
            return session;
        }

    }

}
