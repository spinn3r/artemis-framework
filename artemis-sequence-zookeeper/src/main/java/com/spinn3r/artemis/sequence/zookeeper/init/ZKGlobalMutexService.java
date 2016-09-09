package com.spinn3r.artemis.sequence.zookeeper.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexFactory;
import com.spinn3r.artemis.sequence.NamedMutexFactory;
import com.spinn3r.artemis.sequence.zookeeper.ZKGlobalMutexFactory;
import com.spinn3r.artemis.sequence.zookeeper.ZKNamedMutex;
import com.spinn3r.artemis.sequence.zookeeper.ZKNamedMutexFactory;
import com.spinn3r.artemis.zookeeper.init.ZookeeperConfig;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
@Config( path = "zookeeper.conf",
         required = true,
         implementation = ZookeeperConfig.class )
public class ZKGlobalMutexService extends BaseService {

    private ZKGlobalMutexFactory zkGlobalMutexFactory;

    private ZKNamedMutexFactory zkNamedMutexFactory;

    private final AtomicReferenceProvider<GlobalMutex> globalMutexProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<ZKGlobalMutexFactory> globalMutexFactoryProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<NamedMutexFactory> namedMutexFactoryProvider = new AtomicReferenceProvider<>( null );

    private final ZookeeperConfig zookeeperConfig;

    @Inject
    ZKGlobalMutexService(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }

    @Override
    public void init() {
        provider( GlobalMutex.class, globalMutexProvider );
        provider( GlobalMutexFactory.class, globalMutexFactoryProvider );
        provider( ZKGlobalMutexFactory.class, globalMutexFactoryProvider );
        provider( NamedMutexFactory.class, namedMutexFactoryProvider );
    }

    @Override
    public void start() throws Exception {

        info( "Running with zookeeper config: %s", zookeeperConfig );

        String connectString = StringUtils.join( zookeeperConfig.getServers(), "," );

        // ***** global mutexes

        zkGlobalMutexFactory
            = new ZKGlobalMutexFactory( connectString,
                                        zookeeperConfig.getConnectTimeout(),
                                        zookeeperConfig.getSessionTimeout(),
                                        zookeeperConfig.getNamespace() );

        globalMutexFactoryProvider.set( zkGlobalMutexFactory );
        GlobalMutex globalMutex = zkGlobalMutexFactory.acquire();
        info( "Acquired global mutex value: %s", globalMutex.getValue() );
        globalMutexProvider.set( globalMutex );

        // ***** now the named mutex factory
        zkNamedMutexFactory =
            new ZKNamedMutexFactory( connectString,
                                     zookeeperConfig.getConnectTimeout(),
                                     zookeeperConfig.getSessionTimeout(),
                                     zookeeperConfig.getNamespace() );

        namedMutexFactoryProvider.set( zkNamedMutexFactory );

    }

    @Override
    public void stop() throws Exception {

        if ( zkGlobalMutexFactory != null )
            zkGlobalMutexFactory.close();

        if ( zkNamedMutexFactory != null )
            zkNamedMutexFactory.close();

    }

}
