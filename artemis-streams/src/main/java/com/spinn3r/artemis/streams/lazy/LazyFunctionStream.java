package com.spinn3r.artemis.streams.lazy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A lazy stream evaluates a set of functions but converts them to suppliers
 * first so that we can call findFirst() on the stream so that only the first
 * function is actually evaluated.
 */
public class LazyFunctionStream<T,R> {

    private final T input;

    private final List<Supplier<R>> suppliers = Lists.newArrayList();

    public LazyFunctionStream(T input) {
        Preconditions.checkNotNull( input );
        this.input = input;
    }

    public Stream<Supplier<R>> of (Function<T,R> f0 ) {
        suppliers.add( convert( f0 ) );
        return suppliers.stream();
    }

    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1) {
        suppliers.add( convert( f0 ) );
        suppliers.add( convert( f1 ) );
        return suppliers.stream();
    }

    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2) {
        suppliers.add( convert( f0 ) );
        suppliers.add( convert( f1 ) );
        suppliers.add( convert( f2 ) );
        return suppliers.stream();
    }

    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3) {
        suppliers.add( convert( f0 ) );
        suppliers.add( convert( f1 ) );
        suppliers.add( convert( f2 ) );
        suppliers.add( convert( f3 ) );
        return suppliers.stream();
    }

    private Supplier<R> convert( Function<T,R> f ) {
        return () -> f.apply( input );
    }

}
