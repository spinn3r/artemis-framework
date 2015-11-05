package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;

/**
 *
 */
public class TestOptionalParameter {

    @Test
    public void test1() throws Exception {

        Injector injector = Guice.createInjector( new MyModule() );

        Simple simple = injector.getInstance( Simple.class );


    }

    class MyModule extends AbstractModule {

        @Override
        protected void configure() {

            bind( Simple.class ).to( SimpleImpl.class );

        }
    }

    interface Simple {


    }

    static class SimpleImpl implements Simple {

        private Foo foo;

        @Inject
        SimpleImpl( Foo foo ) {
            this.foo = foo;
        }

    }

    static class Foo {

    }

}
