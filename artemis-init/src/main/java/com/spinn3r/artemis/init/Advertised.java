package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.google.inject.*;
import com.google.inject.util.Modules;
import com.spinn3r.artemis.init.guice.NullModule;
import com.spinn3r.artemis.init.tracer.StandardTracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactorySupplier;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains objects that are advertised by class name.
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
@Deprecated
public class Advertised {

    // TODO: this could/should be refactored into a set of reference objects so
    // that I can get type safety.

    protected Map<Class,Object> advertisements = new ConcurrentHashMap<>();

    protected Map<Class,Class> sources = new ConcurrentHashMap<>();

    protected Module module = new NullModule();

    public TracerFactorySupplier tracerFactorySupplier;

    private Injector injector = Guice.createInjector();

    private List<Module> modules = Lists.newArrayList();

    protected Module effectiveModule = new NullModule();

    public Advertised() {

        // advertise the TracerFactoryProvider so that we can change providers
        // at runtime without using replace.

        tracerFactorySupplier = TracerFactorySupplier.of(new StandardTracerFactory());

        advertise(this.getClass(), TracerFactorySupplier.class, tracerFactorySupplier);

        // by default, advertise ourselves so that we can inject advertised
        // if necessary.
        advertise(this.getClass(), Advertised.class, this);

        // create one injector that we can use everywhere
        //advertise( Injector.class, new Injector( this ) );

    }

    public void useModule( Module module ) {
        this.module = module;
    }

    /**
     * Bind to a set of classes, not just one.  This way we can inject a Set of
     * interfaces.
     */
    //public <T> void advertise( Object source, Classes<T> classes ) {
    //    replace( source.getClass(), classes );
    //}

    /**
     * Advertise the given object for the given class.
     */
    @Deprecated
    public <T, V extends T> void advertise( Object source, Class<T> clazz, V object ) {

        if ( object == null )
            throw new NullPointerException();

        advertise( source.getClass(), clazz, object ) ;

    }

    @Deprecated
    public <T> void provider( Class source, Class<T> clazz, Provider<? extends T> provider ) {
        assertAbsent( clazz, null );
        advertisements.put( clazz, provider );
        sources.put( clazz, source );

    }

    @Deprecated
    public <T, V extends T> void advertise( Class source, Class<T> clazz, V object ) {

        assertAbsent( clazz, null );
        replace( source, clazz, object );

    }

    @Deprecated
    public <T,V extends T> void advertise( Object source, Class<T> clazz, Class<V> impl ) {
        advertise( source.getClass(), clazz, impl );
    }

    @Deprecated
    public <T,V extends T> void advertise( Class source, Class<T> clazz, Class<V> impl ) {
        assertAbsent( clazz, impl );
        advertisements.put( clazz, impl );
        sources.put( clazz, source );

    }

    private void assertAbsent( Class<?> clazz, Class<?> impl ) {

        if ( advertisements.containsKey( clazz ) ) {

            if ( impl != null && advertisements.get( clazz ).equals( impl ) ) {
                // we're just re-advertising the same thing so that's ok.
                return;
            }

            String msg = String.format( "Already advertised: %s from source %s", clazz.getName(), sources.get( clazz ) );
            throw new RuntimeException( msg );
        }

    }

    protected <T, V extends T> void replace( Object source, Class<T> clazz, V object ) {
        replace( source.getClass(), clazz, object );
    }

    protected <T,V extends T> void replace( Class source, Class<T> clazz, Class<V> impl ) {
        advertisements.put( clazz, impl );
        sources.put( clazz, source );
    }

    /**
     * Replace the current TracerFactory with a new TracerFactory. Normally after
     * we've initialized some other tracing system like log4j.
     */
    protected <T, V extends T> void replace( Class source, Class<T> clazz, V object ) {
        advertisements.put( clazz, object );
        sources.put( clazz, source );
    }

    /**
     * Find the given service, or return null if it hasn't yet been advertised.
     */
    @Deprecated
    public <T> T find(Class<T> clazz) {

        return (T)advertisements.get( clazz );
    }

    public int size() {
        return advertisements.size();
    }

    public String format() {

        return new AdvertisedFormatter( this ).format();

    }

    public boolean contains( Class clazz ) {
        return advertisements.containsKey( clazz );
    }

    @Deprecated
    public Injector createInjector() {
        return createInjector(new NullModule());
    }

    public Injector createInjector(Module module) {

        Module newEffectiveModule = Modules.override(effectiveModule).with(module);
        injector = injector.createChildInjector(newEffectiveModule);
        this.effectiveModule = newEffectiveModule;

        return injector;
    }

    @Deprecated
    public <T> T getInstance( Class<T> clazz ) {
        return createInjector().getInstance( clazz );
    }

    /**
     * Convert this to a guice binding module so that we can perform
     * dependency injection on anything advertised.
     *
     */
    public Module toModule() {

        // if we have explicitly used a module, just use it directly.
        if ( module != null ) {
            return module;
        }

        return createModule();

    }

    private Module createModule() {
        return new AdvertisedModule();
    }

    @SuppressWarnings( "unchecked" )
    class AdvertisedModule extends AbstractModule {

        @Override
        protected void configure() {

            for ( Class key : advertisements.keySet() ) {

                Object value = advertisements.get( key );

                if ( value instanceof Class ) {

                    Class clazz = (Class) value;

                    if ( Provider.class.isAssignableFrom( clazz ) ) {
                        bind( key ).toProvider( clazz );
                    } else {
                        bind( key ).to( clazz );
                    }

                } else if ( value instanceof Provider ) {
                    bind( key ).toProvider( (Provider)value );
                } else {

                    bind( key ).toInstance( value );

                }

            }

        }

    }

}
