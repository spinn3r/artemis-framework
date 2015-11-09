package com.spinn3r.artemis.sequence.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.sequence.none.init.NoGlobalMutexService;
import com.spinn3r.artemis.zookeeper.init.ZookeeperService;

/**
 *
 */
@Config( path = "sequence-support.conf",
         required = false,
         implementation = SequenceSupportConfig.class )
public class SequenceSupportService extends BaseService {

    private final SequenceSupportConfig sequenceSupportConfig;

    @Inject
    SequenceSupportService(SequenceSupportConfig sequenceSupportConfig) {
        this.sequenceSupportConfig = sequenceSupportConfig;
    }

    @Override
    public void init() {

        info( "Running with %s", sequenceSupportConfig.getProvider() );

        switch ( sequenceSupportConfig.getProvider() ) {

            case ZOOKEEPER:
                include( ZookeeperService.REF );
                break;

            case NONE:
                include( NoGlobalMutexService.REF );
                break;

            default:
                throw new RuntimeException( "Unknown provider: " + sequenceSupportConfig.getProvider() );

        }


    }

}
