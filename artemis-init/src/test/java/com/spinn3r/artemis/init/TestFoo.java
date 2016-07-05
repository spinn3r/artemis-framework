package com.spinn3r.artemis.init;

import org.junit.Test;

/**
 *
 */
public class TestFoo {

    @Test
    public void test1() throws Exception {

        Foo foo = new Foo.Builder()
                    .setCar("hello")
                    .createFoo();

    }
}
