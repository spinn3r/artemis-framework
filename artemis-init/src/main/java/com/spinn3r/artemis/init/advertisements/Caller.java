package com.spinn3r.artemis.init.advertisements;

/**
 * The caller of a specific set of bindings.  This could be used to start up
 * various core threads, and perform some shared operation, but track which
 * thread is calling the operation.
 *
 * <p>
 * We use this to track which task is making HTTP requests.  If you don't have
 * meaningful values of this class you could use the class calling main() as
 * the default.
 * </p>
 *
 * <p>
 * Another idea is to use a caller with a null value or a value of "unknown".
 * </p>
 *
 */
public class Caller {

    private final String initialValue;

    public Caller( Class<?> clazz ) {
        this( clazz.getName() );
    }

    public Caller( String value ) {
        this.initialValue = value;
    }

    public String get() {
        return backing.get();
    }

    public void set( String value ) {
        backing.set( value );
    }

    @Override
    public String toString() {
        return backing.get();
    }

    private ThreadLocal<String> backing = new ThreadLocal<String>() {

        @Override
        protected String initialValue() {
            return initialValue;
        }

    };


}
