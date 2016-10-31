package com.spinn3r.artemis.util.math;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.*;

public class MeanTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMeanFailure() throws Exception {

        new Mean().of();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMeanFailureWithList() throws Exception {

        new Mean().of(Lists.newArrayList());

    }

    @Test
    public void testMean() throws Exception {
        assertEquals( 50, new Mean().of( 0L, 100L ).intValue() );
        assertEquals( 50, new Mean().of( 50L, 50L ).intValue() );
    }

}