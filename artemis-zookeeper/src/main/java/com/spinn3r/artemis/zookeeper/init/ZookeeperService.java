package com.spinn3r.artemis.zookeeper.init;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.init.ServiceReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 *
 */
@Config( path = "zookeeper.conf",
         required = true,
         implementation = ZookeeperConfig.class )
public class ZookeeperService extends BaseService {

    public static final ServiceReference REF = new ServiceReference( ZookeeperService.class );

    private static final String NAMESPACE = "artemis";

    private final ZookeeperConfig zookeeperConfig;

    private CuratorFramework curatorFramework;

    private AtomicReferenceProvider<CuratorFramework> curatorFrameworkProvider
      = new AtomicReferenceProvider<>( null );

    // I don't think we can retry with ephemeral nodes as the nodes will be gone.
    private RetryPolicy retryPolicy = new RetryNTimes( 0, 0 );

    @Inject
    ZookeeperService(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }

    @Override
    public void init() {
        provider( CuratorFramework.class, curatorFrameworkProvider );
    }

    @Override
    public void start() throws Exception {

        info( "Running with zookeeper zookeeperConfig: %s", zookeeperConfig );

        String connectString = StringUtils.join( zookeeperConfig.getServers(), "," );

        curatorFramework =
          CuratorFrameworkFactory
              .builder()
              .connectString( connectString )
              .retryPolicy( retryPolicy )
              .connectionTimeoutMs( zookeeperConfig.getConnectTimeout() )
              .sessionTimeoutMs( zookeeperConfig.getSessionTimeout() )
              .namespace( NAMESPACE )
              .build();

        curatorFramework.start();

        curatorFrameworkProvider.set( curatorFramework );

    }

    @Override
    public void stop() throws Exception {
        curatorFramework.close();
        curatorFrameworkProvider.set( null );
    }

}
