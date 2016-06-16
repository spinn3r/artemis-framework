package com.spinn3r.artemis.tracepoints.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.tracepoints.Tracepoints;
import com.spinn3r.artemis.tracepoints.TracepointsFactory;

/**
 *
 */
public class TracepointsService extends BaseService {

    private final TracepointsFactory tracepointsFactory;

    @Inject
    TracepointsService(TracepointsFactory tracepointsFactory) {
        this.tracepointsFactory = tracepointsFactory;
    }

    @Override
    public void init() {

        advertise(Tracepoints.class, tracepointsFactory.create());

    }

}
