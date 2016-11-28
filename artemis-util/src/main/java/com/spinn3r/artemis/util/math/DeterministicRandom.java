package com.spinn3r.artemis.util.math;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.util.hashcodes.Hashcodes;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 */
public class DeterministicRandom {

    public static Random create(String key) {

        String hashcode = Hashcodes.getHashcode(key);

        BigInteger bigInteger = new BigInteger(hashcode.getBytes(Charsets.UTF_8));

        return new Random(bigInteger.longValue());

    }

}
