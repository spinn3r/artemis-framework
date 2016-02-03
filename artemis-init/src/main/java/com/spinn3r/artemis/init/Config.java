package com.spinn3r.artemis.init;

import java.lang.annotation.*;

/**
 * The path to the config file , relative to the /etc daemon directory.  So if
 * the daemon name was 'artemis-robot' and the ConfigPath was robot.conf the file
 * would be loaded from /etc/artemis-robot/robot.conf
 */
@Retention( value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@SuppressWarnings( "rawtypes" )
public @interface Config {

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

    /**
     * The class to use for the config object.
     */
    Class<? extends Object> implementation();

}
