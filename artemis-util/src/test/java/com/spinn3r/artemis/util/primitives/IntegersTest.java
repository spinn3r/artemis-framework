package com.spinn3r.artemis.util.primitives;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class IntegersTest {

    @Test(expected = IllegalArgumentException.class)
    public void rangeInvalidRange() throws Exception {

        Integers.range(1 , 0);

    }

    @Test
    public void testEmptyRange() throws Exception {

        assertEquals("[1]", Integers.range(1,1).toString());

    }

    @Test
    public void testBasicRange() throws Exception {

        assertEquals("[1, 2]", Integers.range(1,2).toString());

    }

}