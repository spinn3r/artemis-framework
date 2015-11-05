package com.spinn3r.artemis.init.example;

import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
public class DefaultFirstService extends BaseService {

    @Override
    public void start() throws Exception {
        advertise( First.class, DefaultFirst.class );
    }

}
