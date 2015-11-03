package com.spinn3r.artemis.util.misc;

import java.util.Collection;

/**
 *
 */
public class TerminatableTasks {

    public static void requestTermination( Collection<? extends TerminatableTask> tasks ) {

        for (TerminatableTask task : tasks) {
            task.requestTermination();
        }

    }

}
