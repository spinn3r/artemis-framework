package com.spinn3r.artemis.init;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test that verifies that if we bind() to a custom instance that this version
 * is used and does not attempt to load one off disk or double load a
 * configuration.
 */
public class AutoConfigurationCustomBindingsTest {

    @Test
    public void testLoadFromResources() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .build();
        launcher.launch();

        AddressConfig addressConfig = launcher.getInstance(AddressConfig.class);

        assertEquals("AddressConfig{name='John Smith', street='123 Fake Street', city='San Francisco', state='California'}",
                     addressConfig.toString());

    }

    @Test
    public void testLoadManually() throws Exception {

        AddressConfig addressConfig = new AddressConfig("Alice Brown", "456 Fälschung Strasse", "Berlin", "Berlin");

        Launcher launcher = Launcher.newBuilder()
                                    .setModule(new AbstractModule() {
                                        @Override
                                        protected void configure() {
                                            bind(AddressConfig.class).toInstance(addressConfig);
                                        }
                                    })
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .build();
        launcher.launch();

        assertEquals("AddressConfig{name='Alice Brown', street='456 Fälschung Strasse', city='Berlin', state='Berlin'}",
                     addressConfig.toString());

    }

    @Singleton
    @AutoConfiguration( path="/address.conf", required = false)
    static class AddressConfig {

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

        public AddressConfig() {
        }

        public AddressConfig(String name, String street, String city, String state) {
            this.name = name;
            this.street = street;
            this.city = city;
            this.state = state;
        }

        @Override
        public String toString() {
            return "AddressConfig{" +
                     "name='" + name + '\'' +
                     ", street='" + street + '\'' +
                     ", city='" + city + '\'' +
                     ", state='" + state + '\'' +
                     '}';
        }

    }

}
