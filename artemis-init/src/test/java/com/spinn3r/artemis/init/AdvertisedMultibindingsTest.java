package com.spinn3r.artemis.init;

import com.google.inject.Inject;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.spinn3r.artemis.init.Classes.classes;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AdvertisedMultibindingsTest {

    @Test
    public void test1() throws Exception {

        /*
        Advertised advertised = new Advertised();

        advertised.advertise( this, classes( MyService.class )
                                      .with( Foo0.class )
                                      .and( Foo1.class ) );

        MyObject myObject = advertised.getInstance( MyObject.class );

        assertThat( myObject.services.size(),
                    equalTo( 2 ) );

        assertThat( myObject.services.toString() , containsString( "Foo0" ) );
        assertThat( myObject.services.toString() , containsString( "Foo1" ) );

        assertThat( myObject.services.toString() , equalTo( "[Foo0, Foo1]" ) );

        */

    }

    @Test
    public void testClassesOrder() throws Exception {

        assertThat( classes( MyService.class )
                      .with( Foo0.class )
                      .and( Foo1.class ).toString(),
                    equalTo( "[class com.spinn3r.artemis.init.AdvertisedMultibindingsTest$Foo0, class com.spinn3r.artemis.init.AdvertisedMultibindingsTest$Foo1]" ) );

    }

    static interface MyService {

    }

    static class Foo0 implements MyService {

        @Override
        public String toString() {
            return "Foo0";
        }

    }

    static class Foo1 implements MyService {

        @Override
        public String toString() {
            return "Foo1";
        }

    }

    static class MyObject {

        final Set<MyService> services;

        @Inject
        MyObject(Set<MyService> services) {
            this.services = services;
        }

    }

}