package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import org.junit.Test;

//import com.google.inject.

import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class TestMultiBindings {

    @Test
    public void test1() throws Exception {

        Injector injector = Guice.createInjector( new MyModule() );

        MyClass test = injector.getInstance( MyClass.class );

        assertThat( test.myInterfaces.size(),
                    equalTo( 2 ) );

        assertThat( test.myInterfaces.toString(),
                    equalTo( "[SimpleImpl, SimpleImpl1]" ) );

    }

    class MyModule extends AbstractModule {

        @Override
        protected void configure() {

            Multibinder<Simple> multibinder = Multibinder.newSetBinder( binder(), Simple.class );
            multibinder.addBinding().to( SimpleImpl.class );
            multibinder.addBinding().to( SimpleImpl1.class );

        }

    }

    interface Simple {


    }

    static class SimpleImpl implements Simple {

        @Inject
        SimpleImpl() {

        }

        @Override
        public String toString() {
            return "SimpleImpl";
        }

    }

    static class SimpleImpl1 implements Simple {

        @Inject
        SimpleImpl1() {

        }

        @Override
        public String toString() {
            return "SimpleImpl1";
        }

    }


    static class MyClass {

        Set<Simple> myInterfaces;

        @Inject
        MyClass(Set<Simple> myInterfaces) {
            this.myInterfaces = myInterfaces;
        }
    }


}
