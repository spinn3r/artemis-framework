package com.spinn3r.artemis.init.example;

import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
public class DefaultThirdService extends BaseService {

    @Override
    public void init() {
        advertise( Third.class, DefaultThird.class );
    }

    @Override
    public void start() throws Exception {

    }

}
