package com.spinn3r.artemis.init;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.spinn3r.artemis.init.advertisements.Caller;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdvertisedTest {

    @Test
    public void test1() throws Exception {

        Advertised advertised = new Advertised();

        advertised.advertise( this, FooInterface.class, new FooImpl() );

        assertNotNull( advertised.find( FooInterface.class ) );
        assertEquals( FooImpl.class, advertised.find( FooInterface.class ).getClass() );

    }

    @Test
    @SuppressWarnings( "deprecation" )
    public void testAdvertiseWithDelegation() throws Exception {

        Advertised advertised = new Advertised();

        FooInterface foo =
            advertised.delegate( this.getClass(), FooInterface.class, FooIntermediate.class, FooImpl.class );

        foo.hello();

        assertEquals( foo.getClass(), FooIntermediate.class );

        FooIntermediate fooIntermediate = (FooIntermediate)foo;

        assertTrue( fooIntermediate.isCalled() );

        FooImpl fooImpl = (FooImpl)fooIntermediate.delegate;

        assertTrue( fooImpl.isCalled() );

    }

    @Test
    public void testSingleInstance() {

        // test that when I advertise an instance, that only one copy is created.

        Caller caller = new Caller( "foo" );

        Advertised advertised = new Advertised();

        advertised.advertise( this, Caller.class, caller );
        advertised.advertise( this, Bar.class, DefaultBar.class );

        Injector injector = advertised.createInjector();
        Bar bar = injector.getInstance( Bar.class );

        assertEquals( "foo", bar.getCaller().get() );
        assertTrue( caller == bar.getCaller() );

    }

}

interface FooInterface {

    public void hello();

}

class FooIntermediate implements FooInterface {

    protected FooInterface delegate;

    private boolean called = false;

    @Inject
    FooIntermediate(FooInterface delegate) {
        this.delegate = delegate;
        System.out.printf( "Creating FooIntermediate...\n" );
    }

    @Override
    public void hello() {
        delegate.hello();
        called = true;
    }

    public boolean isCalled() {
        return called;
    }

}

class FooImpl implements FooInterface {

    private boolean called = false;

    FooImpl() {
        System.out.printf( "Creating FooImpl\n" );
    }

    public void hello() {
        System.out.printf( "hello world" );
        called = true;
    }

    public boolean isCalled() {
        return called;
    }
}


interface Bar {

    public Caller getCaller();

}

class DefaultBar implements Bar {

    private Caller caller;

    @Inject
    DefaultBar(Caller caller) {
        this.caller = caller;
    }

    @Override
    public Caller getCaller() {
        return caller;
    }

}