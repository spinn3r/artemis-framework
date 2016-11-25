package com.spinn3r.artemis.util.primitives;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 */
public class Integers {

    /**
     * Return all integers in the range as a list (inclusive).
     */
    public static ImmutableList<Integer> range(int start, int end) {

        Preconditions.checkArgument(end >= start, "End must be >= start");

        List<Integer> result = Lists.newArrayList();

        for (int i = start; i <= end; i++) {
            result.add(i);
        }

        return ImmutableList.copyOf(result);

    }

}
