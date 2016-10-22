package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class AutoConfigurationLoaderTest {

    @Test
    public void testLoadOptionalConfig() throws Exception {

        AutoConfigurationLoader autoConfigurationLoader = createAutoConfigurationLoader();

        autoConfigurationLoader.load(new OptionalAutoConfiguration());

    }

    @Test
    public void testLoadRequiredConfig() throws Exception {

        AutoConfigurationLoader autoConfigurationLoader = createAutoConfigurationLoader();

        autoConfigurationLoader.load(new RequiredAutoConfiguration());

    }

    @Test(expected = AutoConfigurationException.MissingConfigException.class)
    public void testRequiredAutoWithWrongPathConfiguration() throws Exception {

        AutoConfigurationLoader autoConfigurationLoader = createAutoConfigurationLoader();

        autoConfigurationLoader.load(new RequiredAutoWithWrongPathConfiguration());

    }

    @Test(expected = AutoConfigurationException.InvalidPathException.class)
    public void testLoadBrokenPathAutoConfiguration() throws Exception {

        AutoConfigurationLoader autoConfigurationLoader = createAutoConfigurationLoader();

        autoConfigurationLoader.load(new BrokenPathAutoConfiguration());

    }


    private AutoConfigurationLoader createAutoConfigurationLoader() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .build();
        launcher.launch();

        return launcher.getInstance(AutoConfigurationLoader.class);

    }

    @AutoConfiguration( path="/empty.conf", required = false)
    static class OptionalAutoConfiguration {

    }

    @AutoConfiguration( path="/empty.conf", required = true)
    static class RequiredAutoConfiguration {

    }

    @AutoConfiguration( path="/broken-path.conf", required = true)
    static class RequiredAutoWithWrongPathConfiguration {

    }

    @AutoConfiguration( path="   ", required = false)
    static class BrokenPathAutoConfiguration {

    }

}