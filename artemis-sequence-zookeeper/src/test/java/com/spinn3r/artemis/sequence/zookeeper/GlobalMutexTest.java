package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.test.zookeeper.BaseZookeeperTest;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class GlobalMutexTest extends BaseZookeeperTest {

    @Test
    public void test1() throws Exception {

        String connectString = getZookeeperConnectString();

        int sessionTimeout = 30000;

        final CountDownLatch latch = new CountDownLatch( 1 );

        ZooKeeper zooKeeper = new ZooKeeper( connectString, sessionTimeout, new Watcher() {

            @Override
            public void process(WatchedEvent watchedEvent) {

                System.out.printf( "watchedEvent: %s\n", watchedEvent );

                if ( watchedEvent.getState() == Event.KeeperState.SyncConnected ) {
                    latch.countDown();
                }

            }

        } );

        //  /artemis/writers/898948

        System.out.printf( "... is zk connected?\n" );

        latch.await();

        System.out.printf( "We're connected to zookeeper... let's rock and roll.\n" );

    }


}