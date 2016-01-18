package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.JCommander;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SetArgsTest {

    @Test
    public void testWithBooleanArgs() throws Exception {

        SetArgs setArgs = new SetArgs();
        JCommander jc = new JCommander(setArgs);

        jc.parse( "--langs", "en", "fr" );

        assertEquals( "SetArgs{langs=[en, fr]}", setArgs.toString() );

    }

}
