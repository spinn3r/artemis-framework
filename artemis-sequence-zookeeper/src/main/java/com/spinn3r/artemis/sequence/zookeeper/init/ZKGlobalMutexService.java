package com.spinn3r.artemis.sequence.zookeeper.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexFactory;
import com.spinn3r.artemis.sequence.zookeeper.ZKGlobalMutexFactory;
import com.spinn3r.artemis.zookeeper.init.ZookeeperConfig;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
@Config( path = "zookeeper.conf",
         required = true,
         implementation = ZookeeperConfig.class )
public class ZKGlobalMutexService extends BaseService {

    private static final String NAMESPACE = "artemis";

    private ZKGlobalMutexFactory zkGlobalMutexFactory;

    private final AtomicReferenceProvider<GlobalMutex> globalMutexProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<GlobalMutexFactory> globalMutexFactoryProvider = new AtomicReferenceProvider<>( null );

    private final ZookeeperConfig config;

    @Inject
    ZKGlobalMutexService(ZookeeperConfig config) {
        this.config = config;
    }

    @Override
    public void init() {
        provider( GlobalMutex.class, globalMutexProvider );
        provider( GlobalMutexFactory.class, globalMutexFactoryProvider );
    }

    @Override
    public void start() throws Exception {

        info( "Running with zookeeper config: %s", config );

        String connectString = StringUtils.join( config.getServers(), "," );

        zkGlobalMutexFactory
            = new ZKGlobalMutexFactory( connectString,
                                        config.getConnectTimeout(),
                                        config.getSessionTimeout(),
                                        NAMESPACE );

        globalMutexFactoryProvider.set( zkGlobalMutexFactory );
        GlobalMutex globalMutex = zkGlobalMutexFactory.acquire();
        info( "Acquired global mutex value: %s", globalMutex.getValue() );
        globalMutexProvider.set( globalMutex );

    }

    @Override
    public void stop() throws Exception {
        zkGlobalMutexFactory.close();
    }

}
