package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Stage;
import com.spinn3r.artemis.init.example.DefaultFirst;
import com.spinn3r.artemis.init.example.DefaultSecond;
import com.spinn3r.artemis.init.example.First;
import com.spinn3r.artemis.init.example.Second;
import org.junit.Test;

//import com.google.inject.

/**
 *
 */
public class TestStage {

    @Test
    public void test1() throws Exception {

        Guice.createInjector( Stage.TOOL, new MyModule() );

    }

    class MyModule extends AbstractModule {

        @Override
        protected void configure() {

            bind( Second.class ).to( DefaultSecond.class );
            //binder().

        }

        @Provides
        public First getFirst() {
            return new DefaultFirst();
        }

    }

}
