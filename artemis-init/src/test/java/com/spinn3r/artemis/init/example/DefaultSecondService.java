package com.spinn3r.artemis.init.example;

import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
public class DefaultSecondService extends BaseService {

    @Override
    public void start() throws Exception {
        advertise( Second.class, DefaultSecond.class );
    }

}
