package com.spinn3r.artemis.util.words;

import java.util.List;
import java.util.Random;

/**
 * Generate random terms from local unix dictionaries.
 */
public class RandomWordGenerator {

    private final List<String> terms;

    private final Random random = new Random();

    RandomWordGenerator(List<String> terms) {
        this.terms = terms;
    }

    public String generate() {
        return terms.get( random.nextInt( terms.size() ) );
    }

}
