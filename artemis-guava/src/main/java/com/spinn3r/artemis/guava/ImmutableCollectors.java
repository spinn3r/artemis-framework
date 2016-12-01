package com.spinn3r.artemis.guava;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.stream.Collector;

/**
 *
 */
public class ImmutableCollectors {

    /** Collect a stream of elements into an {@link ImmutableList}. */
    public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList() {
        return Collector.of(ImmutableList.Builder::new, ImmutableList.Builder::add,
                            (l, r) -> l.addAll(r.build()), ImmutableList.Builder<T>::build);
    }

    /** Collect a stream of elements into an {@link ImmutableSet}. */
    public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> toImmutableSet() {
        return Collector.of(ImmutableSet.Builder::new, ImmutableSet.Builder::add,
                            (l, r) -> l.addAll(r.build()), ImmutableSet.Builder<T>::build);
    }

}
