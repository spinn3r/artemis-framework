package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.tracer.Tracer;

import java.util.List;

/**
 *
 */
public abstract class BaseService implements Service {

    private Advertised advertised;

    protected Services services;

    protected Tracer tracer;

    protected Includer includer;

    /**
     */
    protected ConfigLoader configLoader = null;

    @Override
    public Advertised getAdvertised() {
        return advertised;
    }

    @Override
    public void setAdvertised(Advertised advertised) {
        this.advertised = advertised;
    }

    @Override
    public Tracer getTracer() {
        return tracer;
    }

    @Override
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    @Override
    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public Services getServices() {
        return services;
    }

    @Override
    public void setServices(Services services) {

        if (services.size() == 0)
            throw new RuntimeException( "no services" );

        this.services = services;
    }

    @Override
    public void setIncluder(Includer includer) {
        this.includer = includer;
    }

    protected void include( ServiceReference... additionalServiceReferences ) {
        includer.include( Lists.newArrayList( additionalServiceReferences ) );
    }

    /**
     * Include any services that should be included dynamically after this one.
     * @param additionalServiceReferences
     */
    protected void include( List<ServiceReference> additionalServiceReferences ) {
        includer.include( additionalServiceReferences );
    }

    protected <T,V extends T> void advertise(Class<T> clazz, V instance) {
        advertised.advertise( this, clazz, instance );
    }

    protected <T,V extends T> void advertise(Class<T> clazz, Class<V> impl ) {
        advertised.advertise( this, clazz, impl );
    }

    protected <T> void provider( Class<T> clazz, Provider<? extends T> provider ) {
        advertised.provider( getClass(), clazz, provider );
    }

    public <T> T delegate( Class<T> clazz, List<Class<? extends T>> list ) {
        return advertised.delegate( getClass(), clazz, list );
    }

    public <T> T delegate( Class<T> clazz, Class<? extends T> c0, Class<? extends T> c1, Class<? extends T> c2, Class<? extends T> c3, Class<? extends T> c4, Class<? extends T> c5 ) {
        return advertised.delegate( getClass(), clazz, c0, c1, c2, c3, c4, c5 );
    }

    public <T> T delegate( Class<T> clazz, Class<? extends T> c0, Class<? extends T> c1, Class<? extends T> c2, Class<? extends T> c3, Class<? extends T> c4 ) {
        return advertised.delegate( getClass(), clazz, c0, c1, c2, c3, c4 );
    }

    public <T> T delegate( Class<T> clazz, Class<? extends T> c0, Class<? extends T> c1, Class<? extends T> c2, Class<? extends T> c3 ) {
        return advertised.delegate( getClass(), clazz, c0, c1, c2, c3 );
    }

    public <T> T delegate( Class<T> clazz, Class<? extends T> c0, Class<? extends T> c1, Class<? extends T> c2 ) {
        return advertised.delegate( getClass(), clazz, c0, c1, c2 );
    }
    public <T> T delegate( Class<T> clazz, Class<? extends T> c0, Class<? extends T> c1 ) {
        return advertised.delegate( getClass(), clazz, c0, c1 );
    }

    protected <T,V extends T> void replace( Class<T> clazz, V instance) {
        advertised.replace( this.getClass(), clazz, instance );
    }

    protected <T,V extends T> void replace(Class<T> clazz, Class<V> impl ) {
        advertised.replace( this.getClass(), clazz, impl );
    }

    protected <T> T find(Class<T> clazz) {
        return getAdvertised().find( clazz );
    }

    protected <T> T require(Class<T> clazz) {
        return getAdvertised().require( clazz );
    }

    protected <T> List<T> interfaces(Class<T> inter) {
        return getAdvertised().interfaces( inter );
    }

    protected Injector createInjector() {
        return getAdvertised().createInjector();
    }

    protected <T> T getInstance( Class<T> clazz ) {
        return createInjector().getInstance( clazz );
    }

    @Override
    public void init() {

    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void info(String format, Object... args) {
        getTracer().info( format, args );
    }

    @Override
    public void warn(String format, Object... args) {
        getTracer().warn( format, args );
    }

    @Override
    public void error(String format, Object... args) {
        getTracer().error( format, args );
    }

    @Override
    public void error(String format, Throwable throwable, Object... args) {
        getTracer().error( format, throwable, args );
    }

}