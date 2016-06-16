package com.spinn3r.artemis.tracepoints.executor;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class BaseExecutor {

    private final AtomicReference<MethodReference> methodReference = new AtomicReference<>();

    /**
     * Lazy init so that we can find the method that called us.
     */
    protected void init() {

        if ( methodReference.get() == null ) {

            synchronized (methodReference) {

                // double check idiom...
                if ( methodReference.get() == null ) {
                    methodReference.set(MethodReferences.caller());
                }

            }

        }

    }

}
