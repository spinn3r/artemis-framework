package com.spinn3r.artemis.init.modular;

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

    public void include( ModularServiceReferences modularServiceReferences ) {
        modularLauncher.include( serviceTypeClazz, modularServiceReferences );
    }

}
