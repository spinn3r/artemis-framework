package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Assert;
import org.junit.Test;

public class PostEncoderTest {

    @Test
    public void testEncodeMultimap() throws Exception {

        ImmutableMultimap<String,String> multimap = ImmutableMultimap.of("animal", "cat",
                                                                         "animal", "dog");

        String encoded = PostEncoder.encode(multimap);

        Assert.assertEquals("animal=cat&animal=dog",
                            encoded);

    }

}