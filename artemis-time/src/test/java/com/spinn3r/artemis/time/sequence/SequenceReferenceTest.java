package com.spinn3r.artemis.time.sequence;

import com.spinn3r.artemis.time.ISO8601;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class SequenceReferenceTest {

    @Test
    public void testProductionSequence() throws Exception {

        SequenceReference sequenceReference = new SequenceReference(1461629328629600000L);

        String timestamp = ISO8601.format(sequenceReference.toDate());

        assertEquals("2016-04-26T00:08:48Z", timestamp);

    }

}