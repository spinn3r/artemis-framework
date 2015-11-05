package com.spinn3r.artemis.http;

import org.junit.Ignore;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class MapToPojoTest {

    @Test
    @Ignore
    public void test1() throws Exception {

        ModelMapper modelMapper = new ModelMapper();

        HashMap<String, String> map = new HashMap<>();
        map.put( "foo", "foo" );
        map.put( "bar", "bar" );
        map.put( "cat_dog", "catDog" );

        MyPojo foo = modelMapper.map(map, MyPojo.class);

        assertEquals( "foo", foo.getFoo() );
        assertEquals( "bar", foo.getBar() );
        assertEquals( "catDog", foo.getCatDog() );

    }

    static class MyPojo {

        private String foo;

        private String bar;

        private String catDog;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }

        public String getCatDog() {
            return catDog;
        }

        public void setCatDog(String catDog) {
            this.catDog = catDog;
        }
    }

}