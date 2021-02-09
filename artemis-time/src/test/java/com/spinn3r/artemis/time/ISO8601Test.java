package com.spinn3r.artemis.time;

import org.junit.Test;

import java.text.ParseException;

import static com.spinn3r.artemis.time.ISO8601.*;
import static org.junit.Assert.*;

public class ISO8601Test {


    @Test
    public void test1() throws ParseException {
        parse("2021-02-01T00:00:00Z");
    }
}
