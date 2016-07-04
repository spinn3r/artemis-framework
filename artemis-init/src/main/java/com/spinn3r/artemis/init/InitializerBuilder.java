package com.spinn3r.artemis.init;

/**
 *
 */
public class InitializerBuilder {

    private final String role;

    private String product = "artemis";

    public InitializerBuilder(String role) {
        this.role = role;
    }

    public InitializerBuilder withProduct( String product ) {
        this.product = product;
        return this;
    }

    public Initializer build() {
        return new Initializer( product, role);
    }

    public static InitializerBuilder forRole( String role ) {
        return new InitializerBuilder( role );
    }

}
