package com.spinn3r.artemis.init;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.FileConfigLoader;

import java.io.File;

/**
 * An initializer which uses a launcher and provides additional bindings we
 * might need such as Caller, Role, etc.
 */
public class Initializer {

    private static final String UNKNOWN = "unknown";

    private ConfigLoader configLoader;

    private Services services = new Services();

    private final Advertised advertised = new Advertised();

    private Launcher launcher;

    public Initializer( String role ) {
        this( role, role );
    }
    public Initializer( String role, String config ) {
        this( role, new FileConfigLoader( new File( String.format( "/etc/artemis-%s", config ) ) ) );
    }


    public Initializer( String role, ConfigLoader configLoader) {

        this.configLoader = configLoader;

        this.launcher = new Launcher( getConfigLoader(), advertised );

        if ( advertised.find( Caller.class ) == null ) {
            advertised.advertise( this, Caller.class, new Caller( UNKNOWN ) );
        }

        if ( advertised.find( Role.class ) == null ) {
            advertised.advertise( this, Role.class, new Role( role ) );
        }

        if ( advertised.find( ConfigLoader.class ) == null )
            advertised.advertise( this, ConfigLoader.class, getConfigLoader() );

        // advertise myself so I can inject it if we want a command to be able
        // to call stop on itself after being initialized.
        advertise( Initializer.class, this );
    }

    public Initializer launch( ServiceReference... references ) throws Exception {
        return launch( new ServiceReferences( references ) );
    }

    public Initializer init( ServiceReferences serviceReferences ) throws Exception {
        launcher.init( serviceReferences );
        return this;
    }

    public Initializer launch( ServiceReferences serviceReferences ) throws Exception {
        launcher.launch( serviceReferences );
        return this;
    }

    public <T, V extends T> void advertise(Class<T> clazz, Class<V> impl) {
        launcher.advertise( clazz, impl );
    }

    public <T, V extends T> void advertise(Class<T> clazz, V object) {
        launcher.advertise( clazz, object );
    }

    public <T, V extends T> void replace(Class<T> clazz, V object) {
        launcher.replace( clazz, object );
    }

    public void stop() throws Exception {
        launcher.stop();
    }

    public Injector getInjector() {
        return launcher.getInjector();
    }

    public Services getServices() {
        return services;
    }

    public Advertised getAdvertised() {
        return advertised;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

}

