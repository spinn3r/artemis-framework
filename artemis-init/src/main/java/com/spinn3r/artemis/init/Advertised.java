package com.spinn3r.artemis.init;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.spinn3r.artemis.init.tracer.StandardTracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains objects that are advertised by class name.
 */
public class Advertised {

    // TODO: this could/should be refactored into a set of reference objects so
    // that I can get type safety.

    protected Map<Class,Object> advertisements = new ConcurrentHashMap<>();

    protected Map<Class,Class> sources = new ConcurrentHashMap<>();

    protected Module module = null;

    public Advertised() {

        replace( Advertised.class, TracerFactory.class, new StandardTracerFactory() );

        // by default, advertise ourselves so that we can inject advertised
        // if necessary.
        advertise( this, Advertised.class, this );

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

    public <T> void provider( Class source, Class<T> clazz, Provider<T> provider ) {
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

    public <T, V extends T> void replace( Object source, Class<T> clazz, V object ) {
        replace( source.getClass(), clazz, object );
    }

    public <T,V extends T> void replace( Class source, Class<T> clazz, Class<V> impl ) {
        advertisements.put( clazz, impl );
        sources.put( clazz, source );
    }

    /**
     * Replace the current TracerFactory with a new TracerFactory. Normally after
     * we've initialized some other tracing system like log4j.  or if we want to wrap
     * another object with a delegate.
     */
    public <T, V extends T> void replace( Class source, Class<T> clazz, V object ) {
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

    public <T> T delegate( Class source,
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

    public <T> T delegate( Class source,
                           Class<T> clazz,
                           Class<? extends T> c0,
                           Class<? extends T> c1,
                           Class<? extends T> c2,
                           Class<? extends T> c3,
                           Class<? extends T> c4 ) {

        List<Class<? extends T>> list = Lists.newArrayList();

        list.add( c0 );
        list.add( c1 );
        list.add( c2 );
        list.add( c3 );
        list.add( c4 );

        return delegate( source, clazz, list );

    }

    public <T> T delegate( Class source,
                           Class<T> clazz,
                           Class<? extends T> c0,
                           Class<? extends T> c1,
                           Class<? extends T> c2,
                           Class<? extends T> c3 ) {

        return delegate( source, clazz, c0, c1, c2, c3, null );

    }

    public <T> T delegate( Class source,
                           Class<T> clazz,
                           Class<? extends T> c0,
                           Class<? extends T> c1,
                           Class<? extends T> c2 ) {

        return delegate( source, clazz, c0, c1, c2, null, null );

    }

    public <T> T delegate( Class source,
                           Class<T> clazz,
                           Class<? extends T> c0,
                           Class<? extends T> c1 ) {

        return delegate( source, clazz, c0, c1, null, null, null );

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

    /**
     * Find all objects that have been advertised with the given interface.
     *
     * @param inter The interface to search for.
     * @param <T>
     * @return
     */
    public <T> List<T> interfaces( Class<T> inter ) {

        List<T> result = Lists.newArrayList();

        for (Object object : advertisements.values()) {

            if ( inter.isAssignableFrom( object.getClass() ) ) {
                result.add( (T)object );
            }

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
        //return Guice.createInjector( Stage.PRODUCTION, toModule() );
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

    class AdvertisedModule extends AbstractModule {

        @Override
        protected void configure() {

            for ( Class key : advertisements.keySet() ) {

                Object value = advertisements.get( key );

                if ( value instanceof Class ) {

                    Class clazz = (Class) value;

                    if (Provider.class.isAssignableFrom( clazz )) {
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
