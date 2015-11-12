package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.JCommander;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ExampleArgsTest {

    @Test
    public void testWithNoArgs() throws Exception {

        ExampleArgs exampleArgs = new ExampleArgs();
        JCommander jc = new JCommander(exampleArgs);

        jc.parse();

        assertEquals( "ExampleArgs{optimize=true}", exampleArgs.toString() );

    }

    @Test
    public void testWithBooleanArgs() throws Exception {

        ExampleArgs exampleArgs = new ExampleArgs();
        JCommander jc = new JCommander(exampleArgs);

        jc.parse( "--optimize=false" );

        assertEquals( "ExampleArgs{optimize=false}", exampleArgs.toString() );

    }

}