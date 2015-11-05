package com.spinn3r.artemis.init.example;

import com.google.inject.Inject;

/**
 *
 */
public class DefaultSecond implements Second {

    private final First first;

    @Inject
    public DefaultSecond(First first) {
        this.first = first;
    }

}
