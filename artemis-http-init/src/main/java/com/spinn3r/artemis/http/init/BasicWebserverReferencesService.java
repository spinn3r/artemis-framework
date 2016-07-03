package com.spinn3r.artemis.http.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.FilterReferences;
import com.spinn3r.artemis.http.RequestLogReferences;
import com.spinn3r.artemis.http.ServletReferences;
import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
public class BasicWebserverReferencesService extends BaseService {

    private ServletReferences servletReferences = ServletReferences.of();
    private FilterReferences filterReferences = new FilterReferences();
    private RequestLogReferences requestLogReferences = new RequestLogReferences();

    @Inject
    BasicWebserverReferencesService() {
    }

    @Override
    public void init() {

        advertise( ServletReferences.class, servletReferences );
        advertise( FilterReferences.class, filterReferences );
        advertise( RequestLogReferences.class, requestLogReferences );

    }

}
