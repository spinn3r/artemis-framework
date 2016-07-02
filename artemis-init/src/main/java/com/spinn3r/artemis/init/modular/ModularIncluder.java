package com.spinn3r.artemis.init.modular;

import com.spinn3r.artemis.init.ServiceType;
import com.spinn3r.artemis.init.ServiceTypeReferences;

/**
 *
 */
public class ModularIncluder {

    private final ModularLauncher modularLauncher;

    private final Class<? extends ServiceType> serviceTypeClazz;

    public ModularIncluder(ModularLauncher modularLauncher, Class<? extends ServiceType> serviceTypeClazz) {
        this.modularLauncher = modularLauncher;
        this.serviceTypeClazz = serviceTypeClazz;
    }

    public void include( ServiceTypeReferences serviceTypeReferences) {
        modularLauncher.include(serviceTypeClazz, serviceTypeReferences);
    }

}
