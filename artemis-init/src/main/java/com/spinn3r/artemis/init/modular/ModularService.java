package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.Service;

/**
 * Tag interface so that modern services that are using Guice configure properly
 * can be referenced in bindings using init.
 */
public interface ModularService extends Service {

}
