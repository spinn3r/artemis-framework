package com.spinn3r.artemis.init;

import java.util.List;

/**
 *
 */
public class Includer {

    private final Launcher launcher;

    private final ServiceReference serviceReference;

    public Includer(Launcher launcher, ServiceReference serviceReference) {
        this.launcher = launcher;
        this.serviceReference = serviceReference;
    }

    public void include( List<ServiceReference> additionalServiceReferences ) {
        launcher.include( serviceReference, additionalServiceReferences );
    }

}
