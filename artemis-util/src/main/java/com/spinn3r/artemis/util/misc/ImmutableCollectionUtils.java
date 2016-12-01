package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ImmutableCollectionUtils {

    /**
     * Shuffle the given list, then return it.
     */
    public static <T> List<T> shuffle(List<T> list ) {
        ArrayList<T> tmp = Lists.newArrayList(list);
        Collections.shuffle(tmp );
        return tmp;
    }

}
