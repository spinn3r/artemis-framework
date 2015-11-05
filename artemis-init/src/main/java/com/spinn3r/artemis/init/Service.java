package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.tracer.Tracer;

/**
 * A service that has start and stop stages.
 */
public interface Service extends Tracer {

    public Advertised getAdvertised();

    public void setAdvertised(Advertised advertised);

    public Tracer getTracer();

    public void setTracer( Tracer tracer );

    public ConfigLoader getConfigLoader();

    public void setConfigLoader(ConfigLoader configLoader);

    public Services getServices();

    public void setServices(Services services);

    public void init();

    public void start() throws Exception;

    public void stop() throws Exception;

}
