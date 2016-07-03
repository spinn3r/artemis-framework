package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.google.inject.*;
import com.spinn3r.artemis.init.tracer.StandardTracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactoryProvider;

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

    protected Module module = null;

    protected TracerFactoryProvider tracerFactoryProvider;

    public Advertised() {

        // advertise the TracerFactoryProvider so that we can change providers
        // at runtime without using replace.

        tracerFactoryProvider = new TracerFactoryProvider(new StandardTracerFactory());

        provider(this.getClass(), TracerFactory.class, tracerFactoryProvider);

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
    public <T, V extends T> void advertise( Object source, Class<T> clazz, V object ) {

        if ( object == null )
            throw new NullPointerException();

        advertise( source.getClass(), clazz, object ) ;

    }

    public <T> void provider( Class source, Class<T> clazz, Provider<? extends T> provider ) {
        assertAbsent( clazz, null );
        advertisements.put( clazz, provider );
        sources.put( clazz, source );

    }

    public <T, V extends T> void advertise( Class source, Class<T> clazz, V object ) {

        assertAbsent( clazz, null );
        replace( source, clazz, object );

    }

    public <T,V extends T> void advertise( Object source, Class<T> clazz, Class<V> impl ) {
        advertise( source.getClass(), clazz, impl );
    }

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
     * we've initialized some other tracing system like log4j.  or if we want to wrap
     * another object with a delegate.
     */
    protected <T, V extends T> void replace( Class source, Class<T> clazz, V object ) {
        advertisements.put( clazz, object );
        sources.put( clazz, source );
    }

    //public <T> void replace( Class source, Classes<T> classes ) {
    //    advertisements.put( classes.getBase(), classes );
    //    sources.put( classes.getBase(), source );
    //}

    /**
     * <p>
     * Perform multiple injections with the same interface.  This allows us to
     * wrap implementations.
     *
     * <p>
     * The implementations provide have methods entering in the first impl
     * parameter and ending in the last param.
     *
     * <p>
     * For example,
     *
     * <p>
     * <code>
     * advertise( ... , Foo.class, FooLogging.class, FooAuth.class, FooImpl.class )
     * </code>
     *
     * <p>
     * This would mean that we would create a Foo interface and advertise it.
     *
     * Methods would enter into FooLogging, be passed to FooAuth, and then finally
     * arrive at FooImpl which is the final delegate.
     *
     */
    @Deprecated
    protected <T> T delegate( Class source,
                              Class<T> clazz,
                              List<Class<? extends T>> list ) {

        List<Class<? extends T>> tmp = Lists.newArrayList();

        for (Class<? extends T> current : list) {
            if ( current != null ) {
                tmp.add( current );
            }
        }

        list = tmp;

        Collections.reverse( list );

        Class<? extends T> first = list.remove( 0 );

        advertise( source, clazz, getInstance( first ) );

        T last = null;

        for (Class<? extends T> impl : list) {

            if ( impl == null )
                continue;

            last = getInstance( impl );

            replace( source, clazz, last );

        }

        return last;

    }
    @Deprecated
    protected  <T> T delegate( Class source,
                               Class<T> clazz,
                               Class<? extends T> c0,
                               Class<? extends T> c1 ) {

        List<Class<? extends T>> list = Lists.newArrayList();

        list.add( c0 );
        list.add( c1 );

        return delegate( source, clazz, list );

    }

    /**
     * Find the given service, or return null if it hasn't yet been advertised.
     */
    public <T> T find(Class<T> clazz) {

        return (T)advertisements.get( clazz );
    }

    /**
     * Find the given service, if it's not present, fail.
     */
    public <T> T require( Class<T> clazz ) {

        T result = find( clazz );

        if ( result == null ) {
            throw new RuntimeException( "Unable to find instance of: " + clazz.getName() );
        }

        return result;

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

    public Injector createInjector() {
        return Guice.createInjector( toModule() );
    }

    /**
     * Verify that all our binding are correct.
     */
    public void verify() {
        Guice.createInjector( Stage.TOOL, toModule() );
    }

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
