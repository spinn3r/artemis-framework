package com.spinn3r.artemis.init.advertisements;

/**
 *
 */
public class Product {

    private final String value;

    public Product(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
