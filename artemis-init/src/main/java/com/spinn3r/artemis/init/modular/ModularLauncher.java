package com.spinn3r.artemis.init.modular;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.inject.*;
import com.spinn3r.artemis.init.*;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.init.modular.stages.StageRunner;
import com.spinn3r.artemis.init.modular.stages.impl.InitStageRunner;
import com.spinn3r.artemis.init.modular.stages.impl.StartStageRunner;
import com.spinn3r.artemis.init.threads.ThreadDiff;
import com.spinn3r.artemis.init.threads.ThreadSnapshot;
import com.spinn3r.artemis.init.threads.ThreadSnapshots;
import com.spinn3r.artemis.init.tracer.StandardTracerFactory;
import com.spinn3r.artemis.init.tracer.Tracer;
import com.spinn3r.artemis.init.tracer.TracerFactory;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ModularLauncher {

    // FIXME: verify that thread snapshots work as before...

    private final ConfigLoader configLoader;

    private final Role role;

    private final Services services = new Services();

    private final Advertised advertised;

    private final AtomicReferenceProvider<Lifecycle> lifecycleProvider
      = new AtomicReferenceProvider<>( Lifecycle.IDLE );

    // ** taken before launch and after stop to help detect improperly
    // shutdown services which have leaky threads
    private ThreadSnapshot threadSnapshot = new ThreadSnapshot();

    private ModularServiceReferences started = new ModularServiceReferences();

    /**
     * The main injector to use after launch is called.
     */
    private Injector injector = null;

    private final ModularServiceReferences modularServiceReferences;

    private final List<Module> modules = Lists.newArrayList();

    ModularLauncher(ConfigLoader configLoader, Role role, Advertised advertised, ModularServiceReferences modularServiceReferences) {
        this.configLoader = configLoader;
        this.role = role;
        this.advertised = advertised;
        this.modularServiceReferences = modularServiceReferences;

        this.modules.add( new StandardDependenciesModule() );

        // advertise myself so I can inject it if we want a command to be able
        // to call stop on itself after being launched.
        advertise( ModularLauncher.class, this );
        provider( Lifecycle.class, lifecycleProvider );
        advertise( Role.class, role );

    }

    public ModularLauncher init() throws Exception {

        Preconditions.checkState( Lifecycle.IDLE.equals( lifecycleProvider.get() ) );

        lifecycleProvider.set( Lifecycle.INITIALIZING );

        launch( new InitStageRunner( configLoader, advertised ) );

        lifecycleProvider.set( Lifecycle.INITIALIZED );

        return this;

    }

    public ModularLauncher start() throws Exception {

        Preconditions.checkState( Lifecycle.IDLE.equals( lifecycleProvider.get() ) );

        lifecycleProvider.set( Lifecycle.STARTING );

        InitStageRunner initStageRunner = new InitStageRunner( configLoader, advertised );
        StartStageRunner startStageRunner = new StartStageRunner();

        launch( (injector, modularService) -> {

            initStageRunner.run( injector, modularService );
            startStageRunner.run( injector, modularService );

        } );

        lifecycleProvider.set( Lifecycle.STARTED );

        return this;

    }


    /**
     * Stop all services started on this launcher.
     */
    public ModularLauncher stop() throws Exception {

        Preconditions.checkState( Lifecycle.STARTED.equals( lifecycleProvider.get() ) );

        lifecycleProvider.set( Lifecycle.STOPPING );

        List<Service> reverse = Lists.newArrayList( services );
        Collections.reverse( reverse );

        for (Service service : services) {

            TracerFactory tracerFactory = injector.getInstance( TracerFactory.class );
            Tracer tracer = tracerFactory.create( service );

            tracer.info( "Stopping service: %s ...", service.getClass().getName() );

            Stopwatch stopwatch = Stopwatch.createStarted();

            service.stop();

            tracer.info( "Stopping service: %s ...done (%s)", service.getClass().getName(), stopwatch.stop() );

        }

        lifecycleProvider.set( Lifecycle.STOPPED );

        ThreadDiff threadDiff = ThreadSnapshots.diff( threadSnapshot, ThreadSnapshots.create() );

        threadDiff.report( advertised.require( TracerFactory.class ).create( this ) );

        threadSnapshot = new ThreadSnapshot();

        return this;

    }
    /**
     * Perform basic init. Mostly used so that test code can verify that
     * bindings are working properly.
     *
     */
    protected ModularLauncher launch( StageRunner stageRunner ) throws Exception {

        for ( ServiceMapping serviceMapping : modularServiceReferences.backing.values() ) {

            Class<? extends ServiceType> modularServiceType = serviceMapping.getSource();
            Class<? extends ModularService> modularServiceClazz = serviceMapping.getTarget();

            ModularConfigLoader modularConfigLoader = new ModularConfigLoader( this, getTracer() );

            ModularServiceReference modularServiceReference = new ModularServiceReference( modularServiceClazz );
            Module configModule = modularConfigLoader.load( modularServiceReference );

            if ( configModule != null ) {
                modules.add( configModule );
            }

            ModularIncluder modularIncluder = new ModularIncluder( this, modularServiceType );

            ServiceDependenciesModule serviceDependenciesModule = new ServiceDependenciesModule( modularIncluder );

            modules.add( serviceDependenciesModule );

            try {

                injector = Guice.createInjector( modules );

                ModularService service = injector.getInstance( modularServiceClazz );

                stageRunner.run( injector, service );

                modules.add( service );
                services.add( service );

                started.put( serviceMapping.getSource(), serviceMapping );

            } catch ( ConfigurationException|CreationException e ) {

                String message = String.format( "Could not create service %s.  \n\nStarted services are: \n%s\nAdvertised bindings are: \n%s",
                                                modularServiceClazz.getName(), started.format(), advertised.format() );

                throw new Exception( message, e );

            }

        }

        // we need to inject one more time so that we can get the last service
        // added to the set of modules.
        injector = Guice.createInjector( modules );

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

    public void include( Class<? extends ServiceType> serviceTypePointer, ModularServiceReferences additionalModularServiceReferences ) {

        modularServiceReferences.include( serviceTypePointer, additionalModularServiceReferences );

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
        return tracerFactory.create( this );
    }

    public void info( String format, Object... args ) {
        getTracer().info( format, args );
    }

    public void error( String format, Object... args ) {
        getTracer().error( format, args );
    }

    public static ModularLauncherBuilder create(ModularServiceReferences modularServiceReferences ) {
        return new ModularLauncherBuilder( new ResourceConfigLoader(), modularServiceReferences );
    }

    public static ModularLauncherBuilder create(ConfigLoader configLoader, ModularServiceReferences modularServiceReferences ) {
        return new ModularLauncherBuilder( configLoader, modularServiceReferences );
    }

    /**
     * Contains standard modules we need for use within within services.
     */
    class StandardDependenciesModule extends AbstractModule {

        @Override
        protected void configure() {
            bind( ModularLauncher.class ).toInstance( ModularLauncher.this );
            bind( Lifecycle.class ).toProvider( lifecycleProvider );
            bind( Role.class ).toInstance( role );
            bind( ConfigLoader.class ).toInstance( configLoader );
            bind( TracerFactory.class ).to( StandardTracerFactory.class );
        }

    }

    /**
     * Modules we create for each service as it's initialized.
     */
    class ServiceDependenciesModule extends AbstractModule {

        private final ModularIncluder modularIncluder;

        public ServiceDependenciesModule(ModularIncluder modularIncluder) {
            this.modularIncluder = modularIncluder;
        }

        @Override
        protected void configure() {
            bind( ModularIncluder.class ).toInstance( modularIncluder );
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
