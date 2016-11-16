package com.spinn3r.artemis.util.words;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Files;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class RandomWordGeneratorFactory {

    public RandomWordGenerator create() throws IOException {
        String data = Files.toUTF8( "/usr/share/dict/american-english" );
        List<String> terms = Lists.newArrayList( data.split( "\n" ) );
        return new RandomWordGenerator(terms );
    }

}
