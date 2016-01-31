package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.tracer.Tracer;

/**
 * A service that has start and stop stages.
 */
public interface Service extends Tracer {

    Advertised getAdvertised();

    void setAdvertised(Advertised advertised);

    Tracer getTracer();

    void setTracer( Tracer tracer );

    ConfigLoader getConfigLoader();

    void setConfigLoader(ConfigLoader configLoader);

    Services getServices();

    void setServices(Services services);

    void init();

    void start() throws Exception;

    void stop() throws Exception;

    void setIncluder(Includer includer);

}
