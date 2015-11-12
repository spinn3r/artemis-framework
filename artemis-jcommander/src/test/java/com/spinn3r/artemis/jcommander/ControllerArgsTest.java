package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.JCommander;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerArgsTest {

    @Test
    public void test1() throws Exception {

        ControllerArgs controllerArgs = new ControllerArgs();
        JCommander jc = new JCommander(controllerArgs);

        jc.parse( "--help" );

        assertTrue( controllerArgs.getHelp() );

        if ( controllerArgs.getHelp() ) {
            jc.usage();
        }

    }

    @Test
    public void testBasicOptions() throws Exception {

        ControllerArgs controllerArgs = new ControllerArgs();
        JCommander jc = new JCommander(controllerArgs);

        String[] args = { "--sourceIndexes", "first", "second", "--targetIndex", "last" };
        jc.parse( args );

        assertEquals( "[first, second]", controllerArgs.getSourceIndexes().toString() );
        assertEquals( "last", controllerArgs.getTargetIndex() );
        assertEquals( true, controllerArgs.getOptimize() );

    }

}