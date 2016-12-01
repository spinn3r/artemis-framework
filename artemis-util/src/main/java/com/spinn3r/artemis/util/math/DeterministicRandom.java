package com.spinn3r.artemis.util.math;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.util.hashcodes.Hashcodes;
import com.spinn3r.artemis.util.misc.ImmutableCollectionUtils;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 */
public class DeterministicRandom {

    public static Random create(String key) {

        String hashcode = Hashcodes.getHashcode(key);

        BigInteger bigInteger = new BigInteger(hashcode.getBytes(Charsets.UTF_8));

        return new Random(bigInteger.longValue());

    }

    public static <T> ImmutableList<T> randomize(String key, ImmutableList<T> list) {
        return ImmutableCollectionUtils.shuffle(list, create(key));
    }

    public static <T> T fetch(String key, ImmutableList<T> list) {
        checkArgument(list.size() > 0);
        ImmutableList<T> shuffled = ImmutableCollectionUtils.shuffle(list, create(key));
        return shuffled.get(0);
    }

}
