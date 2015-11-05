package com.spinn3r.artemis.http.parameters;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import static com.spinn3r.artemis.http.parameters.Parameters.*;

public class ParametersTest {

    @Test
    public void test1() throws Exception {

        // test all the failure cases...
        Map<String,List<String>> params = new HashMap<>();

        assertEquals( 101,
                      fromMap( params ).get( "foo" )
                          .first()
                          .asInt( 101 ) );

        params.put( "foo", Lists.newArrayList( "102") );

        assertEquals( 102,
                      fromMap( params ).get( "foo" )
                        .first()
                        .asInt( 101 ) );

        assertEquals( Lists.newArrayList(),
                      fromMap( params ).get( "asdf" )
                        .values() );


    }

}