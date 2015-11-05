package com.spinn3r.artemis.init.assissted;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.spinn3r.artemis.time.Clock;

/**
 *
 */
public class TestObject {

    private final Clock clock;

    private final String name;

    @Inject
    TestObject( Clock clock,
                @Assisted String name) {
        this.clock = clock;
        this.name = name;
    }
}
