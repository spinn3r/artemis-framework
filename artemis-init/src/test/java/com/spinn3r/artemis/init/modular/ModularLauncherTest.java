package com.spinn3r.artemis.init.modular;

import com.google.inject.AbstractModule;
import org.junit.Test;

public class ModularLauncherTest {

    @Test
    public void testGenericObjectCreation() throws Exception {

        Class<?> clazz = String.class;

        Loader<?> loader = new Loader<>();

        Foo<?> foo = new Foo<>( clazz );

    }

    class Loader<T> {

        public <T extends Object> T create( Class<T> clazz ) throws IllegalAccessException, InstantiationException {
            return clazz.newInstance();
        }

    }

    class Foo<T extends Object> extends AbstractModule {

        private final Class<T> clazz;

        public Foo(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T createInstance() throws IllegalAccessException, InstantiationException {
            return clazz.newInstance();
        }

        @Override
        protected void configure() {
            try {
                bind( clazz ).toInstance( createInstance() );
            } catch (IllegalAccessException|InstantiationException e) {
                throw new RuntimeException( e );
            }
        }

    }

}