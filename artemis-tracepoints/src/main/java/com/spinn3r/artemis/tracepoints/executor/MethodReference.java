package com.spinn3r.artemis.tracepoints.executor;

/**
 *
 */
public class MethodReference {

    private final String classname;

    private final String method;

    private final int lineNumber;

    public MethodReference(String classname, String method, int lineNumber) {
        this.classname = classname;
        this.method = method;
        this.lineNumber = lineNumber;
    }

    public String getClassname() {
        return classname;
    }

    public String getMethod() {
        return method;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return String.format( "%s.%s:%s", classname, method, lineNumber);
    }

}
