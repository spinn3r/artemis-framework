package com.spinn3r.artemis.json;

/**
 *
 */
public class Foo {

    protected final String firstName;

    protected final String lastName;

    protected final String address;

    public Foo(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Foo{" +
                 "firstName='" + firstName + '\'' +
                 ", lastName='" + lastName + '\'' +
                 ", address='" + address + '\'' +
                 '}';
    }
}
