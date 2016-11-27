package com.spinn3r.artemis.util.primitives;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CharactersTest {

    @Test
    public void testRange() throws Exception {

        assertEquals("[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z]",
                     Characters.range('A', 'Z').toString());

    }
}