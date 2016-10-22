package com.spinn3r.artemis.init;

import com.google.inject.Singleton;
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

    @Test
    public void testAddress() throws Exception {

        AutoConfigurationLoader autoConfigurationLoader = createAutoConfigurationLoader();

        AddressAutoConfiguration addressAutoConfiguration = new AddressAutoConfiguration();
        autoConfigurationLoader.load(addressAutoConfiguration);

        assertEquals("AddressAutoConfiguration{name='John Smith', street='123 Fake Street', city='San Francisco', state='California'}",
                     addressAutoConfiguration.toString());

    }

    @Test(expected = AutoConfigurationException.NotSingletonException.class)
    public void testNotSingletonAutoConfiguration() throws Exception {

        AutoConfigurationLoader autoConfigurationLoader = createAutoConfigurationLoader();

        autoConfigurationLoader.load(new NotSingletonAutoConfiguration());

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
    static class NotSingletonAutoConfiguration {

    }

    @Singleton
    @AutoConfiguration( path="/empty.conf", required = false)
    static class OptionalAutoConfiguration {

    }

    @Singleton
    @AutoConfiguration( path="/empty.conf", required = true)
    static class RequiredAutoConfiguration {

    }

    @Singleton
    @AutoConfiguration( path="/broken-path.conf", required = true)
    static class RequiredAutoWithWrongPathConfiguration {

    }

    @Singleton
    @AutoConfiguration( path="   ", required = false)
    static class BrokenPathAutoConfiguration {

    }

    @Singleton
    @AutoConfiguration( path="/address.conf", required = false)
    static class AddressAutoConfiguration {

        private String name;

        private String street;

        private String city;

        private String state;

        public String getName() {
            return name;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        @Override
        public String toString() {
            return "AddressAutoConfiguration{" +
                     "name='" + name + '\'' +
                     ", street='" + street + '\'' +
                     ", city='" + city + '\'' +
                     ", state='" + state + '\'' +
                     '}';
        }

    }

}