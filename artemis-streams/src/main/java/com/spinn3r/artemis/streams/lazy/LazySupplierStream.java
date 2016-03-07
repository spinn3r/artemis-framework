package com.spinn3r.artemis.streams.lazy;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Provides some semantic sugar to avoid generics getting confused.
 */
public class LazySupplierStream {

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2) {
        return Stream.of( s0, s1, s2 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3) {
        return Stream.of( s0, s1, s2, s3 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3, Supplier<R> s4) {
        return Stream.of( s0, s1, s2, s3, s4 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3, Supplier<R> s4, Supplier<R> s5) {
        return Stream.of( s0, s1, s2, s3, s4, s5 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3, Supplier<R> s4, Supplier<R> s5, Supplier<R> s6) {
        return Stream.of( s0, s1, s2, s3, s4, s5, s6 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3, Supplier<R> s4, Supplier<R> s5, Supplier<R> s6, Supplier<R> s7) {
        return Stream.of( s0, s1, s2, s3, s4, s5, s6, s7 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3, Supplier<R> s4, Supplier<R> s5, Supplier<R> s6, Supplier<R> s7, Supplier<R> s8) {
        return Stream.of( s0, s1, s2, s3, s4, s5, s6, s7, s8 );
    }

    public static <R> Stream<Supplier<R>> of (Supplier<R> s0, Supplier<R> s1, Supplier<R> s2, Supplier<R> s3, Supplier<R> s4, Supplier<R> s5, Supplier<R> s6, Supplier<R> s7, Supplier<R> s8, Supplier<R> s9) {
        return Stream.of( s0, s1, s2, s3, s4, s5, s6, s7, s8, s9 );
    }

}


