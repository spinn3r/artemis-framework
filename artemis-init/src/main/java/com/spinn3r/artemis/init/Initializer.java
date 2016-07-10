package com.spinn3r.artemis.init;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.advertisements.Product;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.FileConfigLoader;
import com.spinn3r.artemis.init.guice.NullModule;

import java.io.File;
import java.util.Optional;

import static com.google.common.base.Preconditions.*;

/**
 * An initializer which uses a launcher and provides additional bindings we
 * might need such as Caller, Role, etc.
 */
public class Initializer {

    private static final String UNKNOWN = "unknown";

    private final ConfigLoader configLoader;

    private final Module module;

    private final Launcher launcher;

    private Services services = new Services();

    public Initializer(Product product, Role role, Caller caller, Module module, Optional<ConfigLoader> configLoader) {
        checkNotNull(product);
        checkNotNull(role);
        checkNotNull(caller);
        checkNotNull(module);
        checkNotNull(configLoader);

        this.module = module;

        if ( ! configLoader.isPresent() ) {
            String path = String.format("/etc/%s-%s", product, role);
            System.out.printf("Loading config data from filesystem: %s\n", path);
            this.configLoader = new FileConfigLoader(new File(path));
        } else {
            this.configLoader = configLoader.get();
        }

        this.launcher = Launcher.newBuilder(this.configLoader)
                                .setModule(this.module)
                                .build();

        Advertised advertised = launcher.advertised;

        if ( advertised.find( Product.class ) == null ) {
            advertised.advertise( this, Product.class, product );
        }

        if ( advertised.find( Caller.class ) == null ) {
            advertised.advertise( this, Caller.class, caller );
        }

        if ( advertised.find( Role.class ) == null ) {
            advertised.advertise( this, Role.class, role );
        }

        if ( advertised.find( ConfigLoader.class ) == null ) {
            advertised.advertise(this, ConfigLoader.class, getConfigLoader());
        }

        // advertise myself so I can inject it if we want a command to be able
        // to call stop on itself after being initialized.
        advertised.advertise( this, Initializer.class, this );
    }

    /**
     * Perform basic init. Mostly used so that test code can verify that
     * bindings are working properly.
     *
     */
    public Initializer init( ServiceReferences serviceReferences ) throws Exception {
        launcher.init( serviceReferences );
        return this;
    }

    public Initializer launch( ServiceReference... references ) throws Exception {
        return launch( new ServiceReferences( references ) );
    }

    public Initializer launch( ServiceReferences serviceReferences ) throws Exception {
        launcher.launch( serviceReferences );
        return this;
    }

    public void stop() throws Exception {
        launcher.stop();
    }

    public Injector getInjector() {
        return launcher.getInjector();
    }

    public Services getServices() {
        return services;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public static class Builder {

        private Optional<ConfigLoader> configLoader = Optional.empty();

        private Product product = new Product("artemis");

        private Role role = new Role(UNKNOWN);

        private Caller caller = new Caller(UNKNOWN);

        private Module module = new NullModule();

        public Builder setConfigLoader(ConfigLoader configLoader) {
            this.configLoader = Optional.of(configLoader);
            return this;
        }

        public Builder setConfigLoader(Optional<ConfigLoader> configLoader) {
            this.configLoader = configLoader;
            return this;
        }

        public Builder setRole(String role) {
            return setRole(new Role(role ) );
        }

        public Builder setRole(Class<?> role) {
            return setRole(new Role(role.getName()));
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setCaller(String caller) {
            return setCaller(new Caller(caller));
        }

        public Builder setCaller(Class<?> clazz) {
            return setCaller(new Caller(clazz));
        }

        public Builder setCaller(Caller caller) {
            this.caller = caller;
            return this;
        }

        public Builder setProduct(String product) {
            return setProduct(new Product(product));
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        /**
         * Used so that we can add a custom module for instance variables or
         * other custom/simple bindings that aren't really services. Primarily
         * for testing purposes.
         */
        public Builder setModule(Module module) {
            this.module = module;
            return this;
        }

        public Initializer build() {
            return new Initializer( product, role, caller, module, configLoader);
        }

    }

}

