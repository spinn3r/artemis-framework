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

    private final ConfigLoader configLoader;

    private final Role role;

    private final Services services = new Services();

    private final Advertised advertised;

    private final AtomicReferenceProvider<Lifecycle> lifecycleProvider
      = new AtomicReferenceProvider<>( Lifecycle.IDLE );

    private final AtomicReferenceProvider<ModularIncluder> modularIncluderProvider
      = new AtomicReferenceProvider<>( null );

    // taken before launch and after stop to help detect improperly
    // shutdown services which have leaky threads

    // FIXME: verify that thread snapshots work as before...
    private ThreadSnapshot threadSnapshot = new ThreadSnapshot();

    private ServiceTypeReferences started = new ServiceTypeReferences();

    // The main injector to use after launch is called.
    private Injector injector = null;

    private final ServiceTypeReferences serviceTypeReferences;

    private final List<Module> modules = Lists.newArrayList();

    ModularLauncher(ConfigLoader configLoader, Role role, Advertised advertised, ServiceTypeReferences serviceTypeReferences) {
        this.configLoader = configLoader;
        this.role = role;
        this.advertised = advertised;
        this.serviceTypeReferences = serviceTypeReferences;

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

        TracerFactory tracerFactory = advertised.tracerFactorySupplier.get();
        threadDiff.report( tracerFactory.create( this ) );

        threadSnapshot = new ThreadSnapshot();

        return this;

    }
    /**
     * Perform basic init. Mostly used so that test code can verify that
     * bindings are working properly.
     *
     */
    protected ModularLauncher launch( StageRunner stageRunner ) throws Exception {

        info("Launching services: \n%s", serviceTypeReferences.format() );

        for ( ServiceTypeReference serviceTypeReference : serviceTypeReferences.entries() ) {

            Class<? extends ServiceType> modularServiceType = serviceTypeReference.getSource();
            Class<? extends Service> modularServiceClazz = serviceTypeReference.getTarget();

            ModularConfigLoader modularConfigLoader = new ModularConfigLoader( this, getTracer() );

            ServiceReference serviceReference = new ServiceReference( modularServiceClazz );
            Module configModule = modularConfigLoader.load( serviceReference );

            if ( configModule != null ) {
                modules.add( configModule );
            }

            ModularIncluder modularIncluder = new ModularIncluder( this, modularServiceType );

            ServiceDependenciesModule serviceDependenciesModule = new ServiceDependenciesModule( modularIncluder );

            modules.add( serviceDependenciesModule );

            try {

                //injector = Guice.createInjector( Modules.override( Lists.newArrayList() ).with( modules ) );
                injector = Guice.createInjector( modules );

                Service service = injector.getInstance( modularServiceClazz );

                stageRunner.run( injector, service );

                modules.add( service );
                services.add( service );

                started.put(serviceTypeReference.getSource(), serviceTypeReference);

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

    public ServiceTypeReferences getServiceTypeReferences() {
        return serviceTypeReferences;
    }

    public void include( Class<? extends ServiceType> serviceTypePointer, ServiceTypeReferences additionalServiceTypeReferences) {

        serviceTypeReferences.include(serviceTypePointer, additionalServiceTypeReferences);

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
    public void verify() {
        //FIXME: advertised.createInjector(Stage.TOOL, module);
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
        TracerFactory tracerFactory = advertised.tracerFactorySupplier.get();
        return tracerFactory.create( this );
    }

    public void info( String format, Object... args ) {
        getTracer().info( format, args );
    }

    public void error( String format, Object... args ) {
        getTracer().error( format, args );
    }

    public static ModularLauncherBuilder create(ServiceTypeReferences serviceTypeReferences) {
        return new ModularLauncherBuilder(new ResourceConfigLoader(), serviceTypeReferences);
    }

    public static ModularLauncherBuilder create(ConfigLoader configLoader, ServiceTypeReferences serviceTypeReferences) {
        return new ModularLauncherBuilder(configLoader, serviceTypeReferences);
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
            bind( ModularIncluder.class ).toProvider( modularIncluderProvider );
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
            // FIXME: this is needed so that we can inject an Includer...
            //bind( ModularIncluder.class ).toInstance( modularIncluder );
        }

    }

    public static class ModularLauncherBuilder {

        private final ConfigLoader configLoader;

        private final ServiceTypeReferences serviceTypeReferences;

        private Role role = new Role( "default" );

        private Advertised advertised = new Advertised();

        ModularLauncherBuilder(ConfigLoader configLoader, ServiceTypeReferences serviceTypeReferences) {
            this.configLoader = configLoader;
            this.serviceTypeReferences = serviceTypeReferences;
        }

        @Deprecated
        public ModularLauncherBuilder withAdvertised(Advertised advertised ) {
            this.advertised = advertised;
            return this;
        }

        public ModularLauncherBuilder withRole(String role) {
            return withRole( new Role( role ) );
        }

        public ModularLauncherBuilder withRole(Role role) {
            this.role = role;
            return this;
        }

        public ModularLauncher build() {

            return new ModularLauncher(configLoader, role, advertised, serviceTypeReferences);

        }

    }

}
