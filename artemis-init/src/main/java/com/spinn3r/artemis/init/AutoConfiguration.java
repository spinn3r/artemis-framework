package com.spinn3r.artemis.init;

import java.lang.annotation.*;

/**
 * Marks this object as being automatically configured by the launcher.
 *
 * We load the file from the filesystem, and load the configuration file and
 * update the instance on this object.  Configuration files MUST be @Singleton
 * or an exception will be thrown at runtime.  This is required because we
 * do not want the injector constantly loading files every time a getInstance
 * is called.
 */
@Retention( value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@SuppressWarnings( "rawtypes" )
public @interface AutoConfiguration {

    /**
     * The path to the configuration file, if any.
     *
     * @return
     */
    String path();

    /**
     * Specifies whether the given config is required or not.  If you can use
     * default values then you can set required = false and the values from the
     * in memory config are used.
     * @return
     */
    boolean required() default false;

}
