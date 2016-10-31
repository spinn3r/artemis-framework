package com.spinn3r.artemis.util.math;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.*;

public class MedianTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMedianWithFailure() throws Exception {
        new Median().of(Lists.newArrayList());
    }

    @Test
    public void testWithOneValue() throws Exception {
        assertEquals(1, new Median().of(1L).longValue());
    }


    @Test
    public void testWithTwoValues() throws Exception {
        assertEquals(1.5, new Median().of(1L, 2L).doubleValue(), 0.0);
    }

    @Test
    public void testWithThreeValues() throws Exception {
        assertEquals(2.0, new Median().of(1L, 2L, 3L).doubleValue(), 0.0);
    }

    @Test
    public void testWithFourValues() throws Exception {
        assertEquals(2.5, new Median().of(1L, 2L, 3L, 4L).doubleValue(), 0.0);
    }

}