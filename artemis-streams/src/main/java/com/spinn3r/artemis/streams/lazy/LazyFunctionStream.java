package com.spinn3r.artemis.streams.lazy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A lazy stream evaluates a set of functions but converts them to suppliers
 * first so that we can call findFirst() on the stream so that only the first
 * function is actually evaluated.
 *
 * For now, all the input objects need to be non-null but we might want to change
 * this in the future.
 *
 */
public class LazyFunctionStream<T,R> {

    private List<T> inputs = Lists.newArrayList();

    private final List<Supplier<R>> suppliers = Lists.newArrayList();

    public LazyFunctionStream(T input0) {
        this.inputs = Lists.newArrayList();
        this.inputs.add(input0);
        init();
    }

    public LazyFunctionStream(T input0, T input1) {
        this.inputs = Lists.newArrayList();
        this.inputs.add(input0);
        this.inputs.add(input1);
        init();
    }

    public LazyFunctionStream(T input0, T input1, T input2) {
        this.inputs = Lists.newArrayList();
        this.inputs.add(input0);
        this.inputs.add(input1);
        this.inputs.add(input2);
        init();
    }

    public LazyFunctionStream(List<T> inputs) {
        Preconditions.checkNotNull( inputs );
        this.inputs = inputs;
        init();
    }

    private void init() {

        inputs = inputs.stream()
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

    }

    public Stream<Supplier<R>> of (Function<T,R> f0 ) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );

        registerFunctions(functions);

        return suppliers.stream();

    }

    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );

        registerFunctions(functions);

        return suppliers.stream();

    }

    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );

        registerFunctions(functions);

        return suppliers.stream();

    }

    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );

        registerFunctions(functions);

        return suppliers.stream();

    }
    
    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3, Function<T,R> f4) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );
        functions.add( f4 );

        registerFunctions(functions);

        return suppliers.stream();

    }
    
    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3, Function<T,R> f4, Function<T,R> f5) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );
        functions.add( f4 );
        functions.add( f5 );

        registerFunctions(functions);

        return suppliers.stream();

    }
    
    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3, Function<T,R> f4, Function<T,R> f5, Function<T,R> f6) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );
        functions.add( f4 );
        functions.add( f5 );
        functions.add( f6 );

        registerFunctions(functions);

        return suppliers.stream();

    }
    
    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3, Function<T,R> f4, Function<T,R> f5, Function<T,R> f6, Function<T,R> f7) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );
        functions.add( f4 );
        functions.add( f5 );
        functions.add( f6 );
        functions.add( f7 );

        registerFunctions(functions);

        return suppliers.stream();

    }
    
    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3, Function<T,R> f4, Function<T,R> f5, Function<T,R> f6, Function<T,R> f7, Function<T,R> f8) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );
        functions.add( f4 );
        functions.add( f5 );
        functions.add( f6 );
        functions.add( f7 );
        functions.add( f8 );

        registerFunctions(functions);

        return suppliers.stream();

    }
    
    public Stream<Supplier<R>> of (Function<T,R> f0, Function<T,R> f1, Function<T,R> f2, Function<T,R> f3, Function<T,R> f4, Function<T,R> f5, Function<T,R> f6, Function<T,R> f7, Function<T,R> f8, Function<T,R> f9) {

        List<Function<T,R>> functions = Lists.newArrayList();

        functions.add( f0 );
        functions.add( f1 );
        functions.add( f2 );
        functions.add( f3 );
        functions.add( f4 );
        functions.add( f5 );
        functions.add( f6 );
        functions.add( f7 );
        functions.add( f8 );
        functions.add( f9 );

        registerFunctions(functions);

        return suppliers.stream();

    }

    private void registerFunctions(List<Function<T,R>> functions) {

        if (inputs.size() == 0 ) {
            // shortcut to avoid entering the for loop on no input
            return;
        }

        for (Function<T, R> function : functions) {
            for (T input : inputs) {
                suppliers.add(convert(function, input));
            }
        }

    }

    private Supplier<R> convert( Function<T,R> f, T input ) {
        return () -> f.apply( input );
    }

}
