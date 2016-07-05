package com.spinn3r.artemis.init;

/**
 *
 */
public class Foo {

    private final String car;

    private final String animal;

    public Foo(String car) {
        this.car = car;
        this.animal = "dog";
    }

    public Foo(String car, String animal) {
        this.car = car;
        this.animal = animal;
    }

    public static class Builder {

        private String car = "honda";

        private String animal = "dog";

        public Builder setCar(String car) {
            this.car = car;
            return this;
        }

        public Builder setAnimal(String animal) {
            this.animal = animal;
            return this;
        }

        public Foo createFoo() {
            return new Foo(car,animal);
        }

    }

}
