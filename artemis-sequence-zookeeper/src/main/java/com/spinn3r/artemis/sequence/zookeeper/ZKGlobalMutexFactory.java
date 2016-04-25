package com.spinn3r.artemis.sequence.zookeeper;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexAcquireException;
import com.spinn3r.artemis.sequence.GlobalMutexFactory;
import com.spinn3r.log5j.Logger;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * Factory to create global mutexes, keep track of their state, and expire them
 * if necessary.
 */
public class ZKGlobalMutexFactory implements GlobalMutexFactory, AutoCloseable {

    private static final Logger log = Logger.getLogger();

    public static final int RANGE = 9999;

    private static final Random RANDOM = new Random( System.currentTimeMillis() );
    private static final String BASEDIR = "/mutexes";

    private String connectString;

    private String namespace;

    private CuratorFramework curatorClient;

    //private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

    // I don't think we can retry with ephemeral nodes as the nodes will be gone.
    private RetryPolicy retryPolicy = new RetryNTimes( 0, 0 );

    // the list of acquired mutexes so that we can expire all of them on disconnect.
    private List<ZKGlobalMutex> acquired = Lists.newArrayList();

    public ZKGlobalMutexFactory( String connectString,
                                 int connectionTimeout,
                                 int sessionTimeout,
                                 String namespace )
      throws GlobalMutexAcquireException {

        this.connectString = connectString;
        this.namespace = namespace;

        try {

            curatorClient = CuratorFrameworkFactory
                                .builder()
                                .connectString( connectString )
                                .retryPolicy( retryPolicy )
                                .connectionTimeoutMs( connectionTimeout )
                                .sessionTimeoutMs( sessionTimeout )
                                .namespace( namespace )
                                .build();

            // handle zk disconnects and session expiration.
            curatorClient.getConnectionStateListenable()
              .addListener( new ConnectionStateListener() {
                  @Override

                  public void stateChanged(CuratorFramework client, ConnectionState newState) {

                      if ( newState == ConnectionState.LOST ||
                           newState == ConnectionState.SUSPENDED ) {

                          log.info( "Connection failed to zookeeper: %s", newState );

                          expireAllMutexes();

                      }

                  }
              } );

              curatorClient.start();

            if ( curatorClient.checkExists().forPath( BASEDIR ) == null ) {
                curatorClient.create().forPath( BASEDIR );
            }

        } catch (Exception e) {
            throw new GlobalMutexAcquireException( "Unable to : ", e );
        }

    }

    protected CuratorFramework getCuratorClient() {
        return curatorClient;
    }

    @Override
    public void close() throws Exception {
        expireAllMutexes();
        this.curatorClient.close();
    }

    // mark all mutexes expired.  We need to do this on explicit session close,
    // or when zk disconnects us or sessions expire.
    private void expireAllMutexes() {

        for (ZKGlobalMutex zkGlobalMutex : acquired) {
            zkGlobalMutex.getExpired().set( true );
        }

    }

    @Override
    public GlobalMutex acquire() throws GlobalMutexAcquireException {

        // start from a random part, but cycle through 16384 times so we can terminate reliably..
        int potentialValue = ZKGlobalMutexFactory.nextPotentialValue();

        for (int i = 0; i < RANGE; i++) {

            potentialValue += i;

            // rollover back to zero
            if ( potentialValue > RANGE )
                potentialValue = 0;

            ZKGlobalMutex result = acquire( potentialValue );

            if ( result != null ) {
                acquired.add( result );
                return result;
            }

        }

        throw new GlobalMutexAcquireException( "Unable to acquire mutex.  Too many allocated." );

    }

    // attempt to acquire a mutex lock on the potential path.  Return null if the
    // mutex already exists on this potential path or return the instance..
    protected ZKGlobalMutex acquire( int value ) throws GlobalMutexAcquireException {

        if ( value < 0 || value >= RANGE )
            throw new GlobalMutexAcquireException( "Attempt to acquire mutex outside of range: " + value );

        try {

            String potentialPath = BASEDIR + "/" + Integer.toString( value );

            curatorClient.create()
               .withMode( CreateMode.EPHEMERAL )
               .forPath( potentialPath )
               ;

            return new ZKGlobalMutex( value );

        } catch ( KeeperException.NodeExistsException e ) {
            // this is ok... we weren't able to acquire the path.
            return null;
        } catch ( Exception e ) {
            throw new GlobalMutexAcquireException( "Unable to acquire mutex: ", e );
        }

    }

    protected static String createPath( String directoryPath, int potentialValue ) {
        return Paths.get( directoryPath, Integer.toString( potentialValue ) ).toString();
    }

    protected static int nextPotentialValue() {
        return nextPotentialValue( RANDOM.nextFloat() );
    }

    protected static int nextPotentialValue( float rand ) {
        int value = (int)(rand * RANGE);
        return value;
    }



}
