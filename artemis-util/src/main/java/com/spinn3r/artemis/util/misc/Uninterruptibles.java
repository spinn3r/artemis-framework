package com.spinn3r.artemis.util.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Uninterruptibles {

    public static boolean awaitTermination(ExecutorService executorService, long timeout, TimeUnit timeUnit) {

        try {
            return executorService.awaitTermination( timeout, timeUnit );
        } catch (InterruptedException e) {
            return false;
        }

    }

}
