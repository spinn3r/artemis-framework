package com.spinn3r.artemis.init.guice.singleton;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 */
@Singleton
public class MyService implements Service {

    public static int instances = 0;

    @Inject
    public MyService() {
        ++instances;
    }

}