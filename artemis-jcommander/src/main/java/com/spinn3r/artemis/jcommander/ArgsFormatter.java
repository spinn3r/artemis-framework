package com.spinn3r.artemis.jcommander;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 */
public class ArgsFormatter {

    private static final CustomStyle CUSTOM_STYLE = new CustomStyle();

    public static String format( Object value ) {

        return ReflectionToStringBuilder.toString( value, CUSTOM_STYLE );

    }

    static class CustomStyle extends ToStringStyle {

        public CustomStyle() {
            super();
            this.setContentStart("[");
            this.setFieldSeparator( SystemUtils.LINE_SEPARATOR + "  ");
            this.setFieldSeparatorAtStart(true);
            this.setContentEnd( SystemUtils.LINE_SEPARATOR + "]");
            this.setUseIdentityHashCode( false );
            this.setUseShortClassName( true );

        }

    }

}
