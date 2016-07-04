package com.spinn3r.artemis.init;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.FileConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;

import java.io.File;
import java.util.Optional;

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
        this( "artemis", role, role );
    }

    public Initializer( String role, String config ) {
        this( "artemis", role, config );
    }

    protected Initializer( String product, String role, String config ) {
        this( role, new FileConfigLoader( new File( String.format( "/etc/%s-%s", product, config ) ) ) );
    }

    public Initializer(String role, ConfigLoader configLoader) {

        this.configLoader = configLoader;

        this.launcher = new Launcher( configLoader, advertised );

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

    /**
     * Perform basic init. Mostly used so that test code can verify that
     * bindings are working properly.
     *
     */
    public Initializer init( ServiceReferences serviceReferences ) throws Exception {
        launcher.init( serviceReferences );
        return this;
    }

    public Initializer launch( ServiceReference... references ) throws Exception {
        return launch( new ServiceReferences( references ) );
    }

    public Initializer launch( ServiceReferences serviceReferences ) throws Exception {
        launcher.launch( serviceReferences );
        return this;
    }

    public <T, V extends T> void advertise(Class<T> clazz, Class<V> impl) {
        launcher.advertise( clazz, impl );
    }

    @Deprecated
    public <T, V extends T> void advertise(Class<T> clazz, V object) {
        launcher.advertise( clazz, object );
    }

    @Deprecated
    public <T, V extends T> void replace(Class<T> clazz, V object) {
        // FIXME: this is only used for specifying the Caller...
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

    @Deprecated
    public Advertised getAdvertised() {
        return advertised;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    /**
     * Create a new builder using the {@link ResourceConfigLoader}
     */
    public static Builder newBuilder() {
        return new Builder(new ResourceConfigLoader() );
    }

    public static Builder newBuilder(ConfigLoader configLoader ) {
        return new Builder(configLoader );
    }

    public static class Builder {

        private ConfigLoader configLoader;

        private Optional<Role> role = Optional.empty();

        private Optional<Caller> caller = Optional.empty();

        private Advertised advertised = new Advertised();

        Builder(ConfigLoader configLoader) {
            this.configLoader = configLoader;
        }

        public Builder withRole(Class<?> role) {
            return withRole(new Role(role.getName()));
        }

        public Builder withRole(String role) {
            return withRole( new Role( role ) );
        }

        public Builder withRole(Role role) {
            this.role = Optional.of(role);
            return this;
        }

        public Builder withCaller(Class<?> clazz) {
            return withCaller(new Caller(clazz));
        }

        public Builder withCaller(Caller caller) {
            this.caller = Optional.of(caller);
            return this;
        }

        public Launcher build() {

            Launcher result = new Launcher( configLoader, advertised );

            if (role.isPresent())
                result.advertised.advertise( this, Role.class, role.get() );

            if (caller.isPresent())
                result.advertised.advertise( this, Caller.class, caller.get() );

            return result;

        }

    }

}

