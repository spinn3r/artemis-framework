package com.spinn3r.artemis.tracepoints.executor;

import com.spinn3r.artemis.util.misc.Stack;

/**
 *
 */
public class MethodReferences {

    public static MethodReference caller() {

        StackTraceElement stackTraceElement = Stack.caller();

        return new MethodReference(stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());

    }

}
