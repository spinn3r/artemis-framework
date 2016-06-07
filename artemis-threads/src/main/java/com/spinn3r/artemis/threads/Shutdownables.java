package com.spinn3r.artemis.threads;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Throwables;
import com.spinn3r.log5j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 */

public class Shutdownables {

    private static final Logger log = Logger.getLogger();

    public static void shutdown(ShutdownableIndex shutdownableIndex) throws Exception {

        List<Exception> exceptions = Lists.newArrayList();

        log.info( "Shutting down %,d services: %s...", shutdownableIndex.size(), shutdownableIndex.getName() );

        for (Map.Entry<String, ? extends Shutdownable> entry : shutdownableIndex.entrySet()) {

            String identifier = entry.getKey();
            Shutdownable shutdownable = entry.getValue();

            log.info("Shutting down service: %s...", identifier);

            try {
                shutdownable.shutdown();
            } catch (Exception e) {
                exceptions.add( e );
            }

            log.info("Shutting down service: %s...done", identifier);

        }

        log.info( "Shutting down %,d services: %s...done", shutdownableIndex.size(), shutdownableIndex.getName() );

        if ( exceptions.size() > 0 )
            throw Throwables.createMultiException(exceptions );

    }

    public static void shutdown(Collection<? extends Shutdownable> shutdownableList) throws Exception {

        List<Exception> exceptions = Lists.newArrayList();

        for (Shutdownable shutdownable : shutdownableList) {

            try {
                shutdownable.shutdown();
            } catch (Exception e) {
                exceptions.add( e );
            }

        }

        if ( exceptions.size() > 0 )
            throw Throwables.createMultiException(exceptions );

    }


}
