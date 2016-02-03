package com.spinn3r.artemis.init.modular;

import com.google.common.collect.Lists;
import com.google.inject.*;
import com.spinn3r.artemis.init.*;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.threads.ThreadSnapshot;
import com.spinn3r.artemis.init.tracer.Tracer;
import com.spinn3r.artemis.init.tracer.TracerFactory;

import java.util.List;

/**
 *
 */
public class ModularLauncher {

    private final ConfigLoader configLoader;

    private final Role role;

    private final Services services = new Services();

    private final Advertised advertised;

    private final AtomicReferenceProvider<Lifecycle> lifecycleProvider
      = new AtomicReferenceProvider<>( Lifecycle.STOPPED );

    // ** taken before launch and after stop to help detect improperly
    // shutdown services which have leaky threads
    private ThreadSnapshot threadSnapshot = new ThreadSnapshot();

    private ModularServiceReferences started = new ModularServiceReferences();

    /**
     * The main injector to use after launch is called.
     */
    private Injector injector = null;

    private Tracer tracer = null;

    private final ModularServiceReferences modularServiceReferences;

    private final List<Module> modules = Lists.newArrayList();

    ModularLauncher(ConfigLoader configLoader, Role role, Advertised advertised, ModularServiceReferences modularServiceReferences) {
        this.configLoader = configLoader;
        this.role = role;
        this.advertised = advertised;
        this.modularServiceReferences = modularServiceReferences;

        this.modules.add( new StandardModules() );

        // advertise myself so I can inject it if we want a command to be able
        // to call stop on itself after being launched.
        advertise( ModularLauncher.class, this );
        provider( Lifecycle.class, lifecycleProvider );
        advertise( Tracer.class, tracer );
        advertise( Role.class, role );

    }

    /**
     * Perform basic init. Mostly used so that test code can verify that
     * bindings are working properly.
     *
     */
    public ModularLauncher init() throws Exception {

        if ( lifecycleProvider.get().equals( Lifecycle.WAITING ) ) {

            lifecycleProvider.set( Lifecycle.INITIALIZING );

            modularServiceReferences.backing.entrySet().forEach( entry -> {

                Class<? extends ServiceType> modularServiceType = entry.getKey();
                Class<? extends ModularService> modularServiceClazz = entry.getValue();

                // TODO load the config for this service using something like
                // the serviceInitializer...

                ModularConfigLoader modularConfigLoader = new ModularConfigLoader( this, getTracer() );

                ModularServiceReference modularServiceReference = new ModularServiceReference( modularServiceClazz );
                Module configModule = modularConfigLoader.load( modularServiceReference );

                if ( configModule != null ) {
                    modules.add( configModule );
                }

                try {

                    Injector injector = Guice.createInjector( modules );
                    ModularService service = injector.getInstance( modularServiceClazz );

                    TracerFactory tracerFactory = injector.getInstance( TracerFactory.class );

                    service.setAdvertised( advertised );
                    service.setTracer( tracerFactory.newTracer( service ) );
                    service.setConfigLoader( getConfigLoader() );
//                    // FIXME: this needs a ModularIncluder...
//                    service.setIncluder( new Includer( launcher, serviceReference ) );

                    service.init();

                    modules.add( service );
                    services.add( service );

                    // FIXME: started.put( modularServiceType, modularServiceClazz );

                } catch ( ConfigurationException|CreationException e ) {

                    String message = String.format( "Could not create service %s.  \n\nStarted services are: \n%s\nAdvertised bindings are: \n%s",
                                                    modularServiceClazz.getName(), started.format(), advertised.format() );

//                     FIXME: this is because we're in a stupid forEach...
//                     FIXME: throw new Exception( message, e );

                }

            } );

            lifecycleProvider.set( Lifecycle.INITIALIZED );

        } else {
            throw new IllegalStateException( "Called init at the wrong stage: " + lifecycleProvider.get() );
        }

        return this;

    }

    public ModularLauncher launch() throws Exception {

        // FIXME: don't use ServicesTool
        return launch( modularServiceReferences, (servicesTool) -> {
            servicesTool.init();
            servicesTool.start();
        } );

    }

    /**
     * Launch services by class.  This allows us to use dependency injection to
     * instantiate each one.
     *
     */
    public ModularLauncher launch( ModularServiceReferences modularServiceReferences, LaunchHandler launchHandler ) throws Exception {

// FIXME
//        this.modularServiceReferences = modularServiceReferences;
//
//        info( "Launching services: \n%s", modularServiceReferences.format() );
//
//        lifecycleProvider.set( Lifecycle.STARTING );
//
//        ServiceInitializer serviceInitializer = new ServiceInitializer( this );
//
//        // we iterate with a for loop so that if a service includes another
//        // service we can incorporate it into our pipeline.
//        for (int i = 0; i < serviceReferences.size(); i++) {
//
//            ServiceReference serviceReference = serviceReferences.get( i );
//
//            try {
//
//                serviceInitializer.init( serviceReference );
//
//                Injector injector = getAdvertised().createInjector();
//                Service current = injector.getInstance( serviceReference.getBacking() );
//                launch0( launchHandler, current );
//
//                services.add( current );
//                started.add( serviceReference );
//
//            } catch ( ConfigurationException |CreationException e ) {
//
//                String message = String.format( "Could not create service %s.  \n\nStarted services are: \n%s\nAdvertised bindings are: \n%s",
//                  serviceReference.getBacking().getName(), started.format(), advertised.format() );
//
//                throw new Exception( message, e );
//
//            }
//
//        }
//
//        if ( injector == null ) {
//            // only ever done if we're starting without any services, usually
//            // only when we are in testing mode.  This is really just an empty
//            // injector at this point.
//            injector = createInjector();
//        }
//
//        info( "Now running with the following advertisements: \n%s", advertised.format() );
//
//        lifecycleProvider.set( Lifecycle.STARTED );
//
        return this;

    }

    public void launch0( LaunchHandler launchHandler, Service... services  ) throws Exception {
        launch0( launchHandler, new Services( services ) );
    }

    public void launch0( LaunchHandler launchHandler, Services newServices ) throws Exception {

// FIXME
//        threadSnapshot.addAll( ThreadSnapshots.create() );
//
//        if ( advertised.find( ConfigLoader.class ) == null ) {
//            advertised.advertise( this, ConfigLoader.class, configLoader );
//        }
//
//        this.services.addAll( newServices );
//
//        ServicesTool servicesTool = new ServicesTool( this, newServices );
//
//        launchHandler.onLaunch( servicesTool );
//
//        injector = createInjector();

    }

    /**
     * Stop all services started on this launcher.
     */
    public ModularLauncher stop() throws Exception {

        // FIXME: should be itempotent.

// FIXME
//        lifecycleProvider.set( Lifecycle.STOPPING );
//
//        new ServicesTool( this, services ).stop();
//
//        lifecycleProvider.set( Lifecycle.STOPPED );
//
//        ThreadDiff threadDiff = ThreadSnapshots.diff( threadSnapshot, ThreadSnapshots.create() );
//
//        threadDiff.report( advertised.require( TracerFactory.class ).newTracer( this ) );
//
//        threadSnapshot = new ThreadSnapshot();
//
        return this;

    }

    public ThreadSnapshot getThreadSnapshot() {
        return threadSnapshot;
    }

    public Services getServices() {
        return services;
    }

    @Deprecated
    public Advertised getAdvertised() {
        return advertised;
    }

    public ModularServiceReferences getModularServiceReferences() {
        return modularServiceReferences;
    }

    public void include( ModularServiceReference currentModularServiceReference, List<ModularServiceReference> additionalModularServiceReferences ) {
//FIXME
//        this.modularServiceReferences.include( currentModularServiceReference, additionalModularServiceReferences );
    }

    @Deprecated
    public <T> void provider( Class<T> clazz, Provider<T> provider ) {
        getAdvertised().provider( this.getClass(), clazz, provider );
    }

    @Deprecated
    public <T,V extends T> void advertise( Class<T> clazz, Class<V> impl ) {
        getAdvertised().advertise( this, clazz, impl );
    }

    @Deprecated
    public <T, V extends T> void advertise( Class<T> clazz, V object ) {
        getAdvertised().advertise( this, clazz, object );
    }

    @Deprecated
    public <T, V extends T> void replace( Class<T> clazz, V object ) {
        getAdvertised().replace( this, clazz, object );
    }

    @Deprecated
    public void verify() {
        getAdvertised().verify();;
    }

    protected Injector createInjector() {
        return getAdvertised().createInjector();
    }

    public Injector getInjector() {
        return injector;
    }

    public <T> T getInstance( Class<T> clazz ) {
        return createInjector().getInstance( clazz );
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    private Tracer getTracer() {
        TracerFactory tracerFactory = advertised.require( TracerFactory.class );
        return tracerFactory.newTracer( this );
    }

    public void info( String format, Object... args ) {
        getTracer().info( format, args );
    }

    public void error( String format, Object... args ) {
        getTracer().error( format, args );
    }

    public static ModularLauncherBuilder create(ConfigLoader configLoader, ModularServiceReferences modularServiceReferences ) {
        return new ModularLauncherBuilder( configLoader, modularServiceReferences );
    }

    class StandardModules extends AbstractModule {

        @Override
        protected void configure() {
            bind( ModularLauncher.class ).toInstance( ModularLauncher.this );
            bind( Lifecycle.class ).toProvider( lifecycleProvider );
            bind( Tracer.class ).toInstance( tracer );
            bind( Role.class ).toInstance( role );
        }

    }

    public static class ModularLauncherBuilder {

        private final ConfigLoader configLoader;

        private final ModularServiceReferences modularServiceReferences;

        private Role role = new Role( "default" );

        private Advertised advertised = new Advertised();

        ModularLauncherBuilder(ConfigLoader configLoader, ModularServiceReferences modularServiceReferences) {
            this.configLoader = configLoader;
            this.modularServiceReferences = modularServiceReferences;
        }

        @Deprecated
        public ModularLauncherBuilder withAdvertised(Advertised advertised ) {
            this.advertised = advertised;
            return this;
        }

        public ModularLauncherBuilder withRole(String role ) {
            return withRole( new Role( role ) );
        }

        public ModularLauncherBuilder withRole(Role role ) {
            this.role = role;
            return this;
        }

        public ModularLauncher build() {

            return new ModularLauncher( configLoader, role, advertised, modularServiceReferences );

        }

    }

}
