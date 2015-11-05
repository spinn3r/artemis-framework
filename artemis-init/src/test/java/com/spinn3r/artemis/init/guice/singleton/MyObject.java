package com.spinn3r.artemis.init.guice.singleton;

import com.google.inject.Inject;

/**
 *
 */
public class MyObject {

    private final Service service;

    @Inject
    public MyObject(Service service) {
        this.service = service;
    }

}
