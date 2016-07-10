package com.spinn3r.artemis.init;

import com.google.inject.Injector;

import static com.google.common.base.Preconditions.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class TestLauncher {

    private final Launcher launcher;

    private final Injector injector;

    public TestLauncher(Launcher launcher) {
        this.launcher = launcher;
        this.injector = launcher.getInjector();
    }

    public <T> SpyReplacement<T>
    replaceWithSpy( Class<T> clazz ) {
        T instance = injector.getInstance( clazz );
        launcher.replace( clazz, spy( instance ) );
        return new SpyReplacement<>( instance );
    }

    public Injector createInjector() {
        return launcher.createInjector();
    }

    public class SpyReplacement<T> {

        private final T delegate;

        public SpyReplacement(T delegate) {
            this.delegate = delegate;
        }

        public <T> void expecting( Class<T> clazz ) {

            checkArgument( delegate.getClass().equals( clazz ), String.format( "Invalid classes %s vs %s", delegate.getClass(), clazz.getClass() ) );

        }

    }

}
