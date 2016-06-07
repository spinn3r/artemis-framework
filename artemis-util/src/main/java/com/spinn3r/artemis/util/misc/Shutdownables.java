package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class Shutdownables {

    public void shutdown(Collection<Shutdownable> shutdownableList) throws Exception {

        for (Shutdownable shutdownable : shutdownableList) {

            List<Exception> exceptions = Lists.newArrayList();

            try {
                shutdownable.shutdown();
            } catch (Exception e) {
                exceptions.add( e );
            }

            if ( exceptions.size() > 0 )
                throw Throwables.createMultiException(exceptions );

        }

    }

}
