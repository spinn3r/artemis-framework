package com.spinn3r.artemis.streams.lazy;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class LazyFunctionStreamTest {

    @Test
    public void testFirstSuccessful() throws Exception {

        TestFunction<String,String> f0 = new TestFunction<>( "0" );
        TestFunction<String,String> f1 = new TestFunction<>( "1" );

        LazyFunctionStream<String,String> lazyFunctionStream = new LazyFunctionStream<>( "hello" );

        Stream<Supplier<String>> of = lazyFunctionStream.of( f0, f1 );

        Optional<String> result = of.map( Supplier::get )
           .filter( Objects::nonNull )
           .findFirst();

        assertEquals( "0", result.get() );
        assertTrue( f0.called );
        assertFalse( f1.called );

    }

    @Test
    public void testFirstFailed() throws Exception {

        TestFunction<String,String> f0 = new TestFunction<>( null );
        TestFunction<String,String> f1 = new TestFunction<>( "1" );

        LazyFunctionStream<String,String> lazyFunctionStream = new LazyFunctionStream<>( "hello" );

        Stream<Supplier<String>> of = lazyFunctionStream.of( f0, f1 );

        Optional<String> result = of.map( Supplier::get )
                                    .filter( Objects::nonNull )
                                    .findFirst();

        assertEquals( "1", result.get() );
        assertTrue( f0.called );
        assertTrue( f1.called );

    }

    @Test
    public void testAllFailed() throws Exception {

        TestFunction<String,String> f0 = new TestFunction<>( null );
        TestFunction<String,String> f1 = new TestFunction<>( null );

        LazyFunctionStream<String,String> lazyFunctionStream = new LazyFunctionStream<>( "hello" );

        Stream<Supplier<String>> of = lazyFunctionStream.of( f0, f1 );

        Optional<String> result = of.map( Supplier::get )
                                    .filter( Objects::nonNull )
                                    .findFirst();

        assertFalse( result.isPresent() );
        assertTrue( f0.called );
        assertTrue( f1.called );

    }

    class TestFunction<T,R> implements Function<T,R> {

        boolean called = false;

        private final R result;

        public TestFunction(R result) {
            this.result = result;
        }

        @Override
        public R apply(T t) {
            this.called = true;
            return result;
        }

    }

}