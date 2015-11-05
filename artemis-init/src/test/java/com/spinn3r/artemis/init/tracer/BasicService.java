package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
public class BasicService extends BaseService {

    @Override
    public void start() throws Exception {
        tracer.info( "Within our tracer: %s", "hello world" );
    }

    @Override
    public void stop() throws Exception {

    }

}
