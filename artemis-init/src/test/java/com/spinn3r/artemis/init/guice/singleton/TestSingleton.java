package com.spinn3r.artemis.init.guice.singleton;

import com.google.inject.*;
import com.spinn3r.artemis.init.example.DefaultFirst;
import com.spinn3r.artemis.init.example.First;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class TestSingleton {

    @Test
    public void test1() throws Exception {

        Injector injector = Guice.createInjector( new MyModule() );

        injector.getInstance( MyObject.class );
        injector.getInstance( MyObject.class );

        assertEquals( 1, MyService.instances );

    }

    class MyModule extends AbstractModule {

        @Override
        protected void configure() {

        }

        @Provides
        public First getFirst() {
            return new DefaultFirst();
        }

    }

}
