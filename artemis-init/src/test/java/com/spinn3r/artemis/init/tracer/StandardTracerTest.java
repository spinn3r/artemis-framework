package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Test;

import static com.spinn3r.artemis.init.Services.ref;

public class StandardTracerTest {

    @Test
    public void test1() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();

        Launcher launcher = Launcher.newBuilder(configLoader )
                                    .withRole( "test" )
                                    .build();

        launcher.launch( ref( BasicService.class ) );

    }

}