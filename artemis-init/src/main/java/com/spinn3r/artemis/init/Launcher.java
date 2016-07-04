package com.spinn3r.artemis.init;

import com.google.inject.*;
import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.init.guice.NullModule;
import com.spinn3r.artemis.init.threads.ThreadDiff;
import com.spinn3r.artemis.init.threads.ThreadSnapshot;
import com.spinn3r.artemis.init.threads.ThreadSnapshots;
import com.spinn3r.artemis.init.tracer.Tracer;
import com.spinn3r.artemis.init.tracer.TracerFactory;

import java.util.List;
import java.util.Optional;

/**
 *
 * Launches all services for a daemon.
 *
 */
public class Launcher {

    private ConfigLoader configLoader;

    private final Services services = new Services();

    protected final Advertised advertised;

    private final AtomicReferenceProvider<Lifecycle> lifecycleProvider
      = new AtomicReferenceProvider<>( Lifecycle.STOPPED );

    // taken before launch() and after stop() to help detect improperly
    // shutdown services.
    private ThreadSnapshot threadSnapshot = new ThreadSnapshot();

    private ServiceReferences started = new ServiceReferences();

    /**
     * The main injector to use after launch is called.
     */
    private Injector injector = null;

    private ServiceReferences serviceReferences;

    private Module module = new NullModule();

    public Launcher(ConfigLoader configLoader, Advertised advertised ) {
        this.configLoader = configLoader;
        this.advertised = advertised;

        // advertise myself so I can inject it if we want a command to be able
        // to call stop on itself after being launched.
        advertise( Launcher.class, this );
        provider( Lifecycle.class, lifecycleProvider );
    }

    /**
     * Perform basic init. Mostly used so that test code can verify that
     * bindings are working properly.
     *
     */
    public Launcher init( ServiceReferences references ) throws Exception {
        return launch( references, ServicesTool::init );
    }

    public Launcher launch() throws Exception {
        return launch( new ServiceReferences() );
    }

    public Launcher launch( ServiceReference... references ) throws Exception {

        return launch( new ServiceReferences( references ) );

    }

    public Launcher launch( ServiceReferences references ) throws Exception {

        return launch( references, (servicesTool) -> {
                servicesTool.init();
                servicesTool.start();
        } );

    }

    @SuppressWarnings("unchecked")
    public Launcher launch( Class<? extends Service>... services ) throws Exception {

        ServiceReferences serviceReferences = new ServiceReferences();

        for (Class<? extends Service> service : services) {
            serviceReferences.add( service );
        }

        return launch( serviceReferences );

    }

    /**
     * Launch services by class.  This allows us to use dependency injection to
     * instantiate each one.
     *
     */
    public Launcher launch( ServiceReferences serviceReferences, LaunchHandler launchHandler ) throws Exception {

        this.serviceReferences = serviceReferences;

        info( "Launching services: \n%s", serviceReferences.format() );

        lifecycleProvider.set( Lifecycle.STARTING );

        ServiceInitializer serviceInitializer = new ServiceInitializer( this );

        // we iterate with a for loop so that if a service includes another
        // service we can incorporate it into our pipeline.
        for (int i = 0; i < serviceReferences.size(); i++) {

            ServiceReference serviceReference = serviceReferences.get( i );

            try {

                serviceInitializer.init( serviceReference );

                Injector injector = createInjector();
                Service current = injector.getInstance( serviceReference.getBacking() );
                launch0( launchHandler, current );

                services.add( current );
                started.add( serviceReference );

            } catch ( ConfigurationException|CreationException e ) {

                String message = String.format( "Could not create service %s.  \n\nStarted services are: \n%s\nAdvertised bindings are: \n%s",
                                                serviceReference.getBacking().getName(), started.format(), advertised.format() );

                throw new Exception( message, e );

            }

        }

        if ( injector == null ) {
            // only ever done if we're starting without any services, usually
            // only when we are in testing mode.  This is really just an empty
            // injector at this point.
            injector = createInjector();
        }

        info( "Now running with the following advertisements: \n%s", advertised.format() );

        lifecycleProvider.set( Lifecycle.STARTED );

        return this;

    }

    public void launch0( LaunchHandler launchHandler, Service... services  ) throws Exception {
        launch0( launchHandler, new Services( services ) );
    }

    public void launch0( LaunchHandler launchHandler, Services newServices ) throws Exception {

        threadSnapshot.addAll( ThreadSnapshots.create() );

        if ( advertised.find( ConfigLoader.class ) == null ) {
            advertised.advertise( this, ConfigLoader.class, configLoader );
        }

        this.services.addAll( newServices );

        ServicesTool servicesTool = new ServicesTool( this, newServices );

        launchHandler.onLaunch( servicesTool );

        injector = createInjector();

    }

    /**
     * Stop all services started on this launcher.
     */
    public Launcher stop() throws Exception {

        lifecycleProvider.set( Lifecycle.STOPPING );

        new ServicesTool( this, services ).stop();

        lifecycleProvider.set( Lifecycle.STOPPED );

        ThreadDiff threadDiff = ThreadSnapshots.diff( threadSnapshot, ThreadSnapshots.create() );

        TracerFactory tracerFactory = advertised.tracerFactorySupplier.get();
        threadDiff.report( tracerFactory.create( this ) );

        threadSnapshot = new ThreadSnapshot();

        return this;
    }

    public ThreadSnapshot getThreadSnapshot() {
        return threadSnapshot;
    }

    public Services getServices() {
        return services;
    }

    public ServiceReferences getServiceReferences() {
        return serviceReferences;
    }

    public void include( ServiceReference currentServiceReference, List<ServiceReference> additionalServiceReferences ) {
        this.serviceReferences.include( currentServiceReference, additionalServiceReferences );
    }

    @Deprecated
    public <T> void provider( Class<T> clazz, T impl ) {
        advertised.provider( this.getClass(), clazz, new AtomicReferenceProvider<>(impl) );
    }

    @Deprecated
    public <T> void provider( Class<T> clazz, Provider<T> provider ) {
        advertised.provider( this.getClass(), clazz, provider );
    }

    @Deprecated
    public <T,V extends T> void advertise( Class<T> clazz, Class<V> impl ) {
        advertised.advertise( this, clazz, impl );
    }

    @Deprecated
    public <T, V extends T> void advertise( Class<T> clazz, V object ) {
        advertised.advertise( this, clazz, object );
    }

    @Deprecated
    public <T, V extends T> void replace( Class<T> clazz, V object ) {
        advertised.replace( this, clazz, object );
    }

    public void verify() {
        advertised.verify();;
    }

    protected Injector createInjector() {
        return advertised.createInjector(module);
    }

    public Injector getInjector() {
        return injector;
    }

    public <T> T getInstance( Class<T> clazz ) {
        return getInjector().getInstance( clazz );
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    private Tracer getTracer() {
        TracerFactory tracerFactory = advertised.tracerFactorySupplier.get();
        return tracerFactory.create( this );
    }

    public void info( String format, Object... args ) {
        getTracer().info( format, args );
    }

    public void error( String format, Object... args ) {
        getTracer().error( format, args );
    }

    /**
     * Create a new builder using the {@link ResourceConfigLoader}
     */
    public static Builder newBuilder() {
        return new Builder(new ResourceConfigLoader());
    }

    public static Builder newBuilder(ConfigLoader configLoader) {
        return new Builder(configLoader);
    }

    public static class Builder {

        private ConfigLoader configLoader;

        private Role role = new Role( "default" );

        private Optional<Caller> caller = Optional.empty();

        private Advertised advertised = new Advertised();

        private Module module = new NullModule();

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
            this.role = role;
            return this;
        }

        public Builder withCaller(Class<?> clazz) {
            return withCaller(new Caller(clazz));
        }

        public Builder withCaller(Caller caller) {
            this.caller = Optional.of(caller);
            return this;
        }

        /**
         * Used so that we can add a custom module for instance variables or
         * other custom/simple bindings that aren't really services. Primarily
         * for testing purposes.
         */
        public Builder withModule(Module module) {
            this.module = module;
            return this;
        }

        public Launcher build() {

            Launcher result = new Launcher( configLoader, advertised );

            result.module = module;
            result.advertised.advertise( this, Role.class, role );

            if (caller.isPresent())
                result.advertised.advertise( this, Caller.class, caller.get() );

            return result;

        }

    }

}
