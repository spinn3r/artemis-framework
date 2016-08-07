package com.spinn3r.artemis.util;

import com.google.common.collect.ImmutableList;

import java.util.Random;

/**
 *
 */
public class Randomizer<T> {

    private final Random random;

    private final ImmutableList<T> backing;

    public Randomizer(ImmutableList<T> backing) {
        this(new Random(), backing);
    }

    public Randomizer(Random random, ImmutableList<T> backing) {
        this.random = random;
        this.backing = backing;
    }

    public T fetch() {
        return backing.get( random.nextInt( backing.size() ) );
    }

}
