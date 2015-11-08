package com.spinn3r.artemis.init;

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

    public void include( Class<? extends Service> serviceClass ) {
        launcher.include( serviceReference, serviceClass );
    }

}
