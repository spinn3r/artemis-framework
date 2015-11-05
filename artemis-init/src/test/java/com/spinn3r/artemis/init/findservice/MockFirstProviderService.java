package com.spinn3r.artemis.init.findservice;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;

/**
 *
 */
@Config( path = "first.conf",
                    required = false,
                    implementation = FirstProviderConfig.class )
public class MockFirstProviderService extends BaseService {

    @Override
    public void start() throws Exception {

        advertise( FirstProvider.class, new MockFirstProvider() );
    }

    @Override
    public void stop() throws Exception {

    }


}
