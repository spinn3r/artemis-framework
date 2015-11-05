package com.spinn3r.artemis.init.guice.delegator;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;

/**
 *
 */
public class TestDelegator {

    @Test
    public void testDelegator() throws Exception {

        Injector injector = Guice.createInjector( new MyModule() );

        injector.getInstance( Emailer.class ).send( "hello world" );

    }

    class MyModule extends AbstractModule {

        @Override
        protected void configure() {
            bind( Emailer.class ).to( EmailerDelegator.class );
        }

    }

}
