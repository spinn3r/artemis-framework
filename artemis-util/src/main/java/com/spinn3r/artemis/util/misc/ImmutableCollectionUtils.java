package com.spinn3r.artemis.util.misc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class ImmutableCollectionUtils {

    /**
     * Shuffle the given list, then return it.
     */
    public static <T> ImmutableList<T> shuffle(List<T> list) {
        ArrayList<T> tmp = Lists.newArrayList(list);
        Collections.shuffle(tmp);
        return ImmutableList.copyOf(tmp);
    }

    public static <T> ImmutableList<T> shuffle(List<T> list, Random random) {
        ArrayList<T> tmp = Lists.newArrayList(list);
        Collections.shuffle(tmp, random);
        return ImmutableList.copyOf(tmp);
    }

}
