package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.tracer.Tracer;

import java.util.List;

/**
 *
 */
public abstract class BaseService extends AbstractModule implements Service {

    private Advertised advertised;

    protected Tracer tracer;

    protected Includer includer;

    protected ConfigLoader configLoader = null;

    @Override
    public void setAdvertised(Advertised advertised) {
        this.advertised = advertised;
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
    public void setIncluder(Includer includer) {
        this.includer = includer;
    }

    protected void include( ServiceReference... additionalServiceReferences ) {
        includer.include( Lists.newArrayList( additionalServiceReferences ) );
    }

    /**
     * Include any services that should be included dynamically after this one.
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

    @Deprecated
    protected <T,V extends T> void replace( Class<T> clazz, V instance) {
        advertised.replace( this.getClass(), clazz, instance );
    }

    @Deprecated
    protected <T,V extends T> void replace(Class<T> clazz, Class<V> impl ) {
        advertised.replace( this.getClass(), clazz, impl );
    }

    @Deprecated
    protected Injector createInjector() {
        return advertised.createInjector();
    }

    @Override
    public void init() {

    }

    @Override
    protected void configure() {

    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void info(String format, Object... args) {
        tracer.info( format, args );
    }

    @Override
    public void warn(String format, Object... args) {
        tracer.warn( format, args );
    }

    @Override
    public void error(String format, Object... args) {
        tracer.error( format, args );
    }

    @Override
    public void error(String format, Throwable throwable, Object... args) {
        tracer.error( format, throwable, args );
    }

}