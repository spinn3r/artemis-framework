package com.spinn3r.artemis.init.guice;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.spinn3r.artemis.init.example.DefaultFirst;
import com.spinn3r.artemis.init.example.DefaultSecond;
import com.spinn3r.artemis.init.example.First;
import com.spinn3r.artemis.init.example.Second;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

//import com.google.inject.

/**
 *
 */
public class TestProvides implements Provider<First> {

    private First first = new DefaultFirst();

    @Override
    public First get() {
        return first;
    }

    @Test
    public void test1() throws Exception {

        Guice.createInjector( Stage.TOOL, new MyModule() );

    }

    class MyModule extends AbstractModule {

        @Override
        protected void configure() {

            bind( First.class ).toProvider( TestProvides.class );
            bind( Second.class ).to( DefaultSecond.class );

        }

    }

}
