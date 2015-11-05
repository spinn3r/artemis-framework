package com.spinn3r.artemis.http;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import org.eclipse.jetty.servlet.FilterHolder;

/**
 *
 */
public class FilterReference {

    private FilterHolder filterHolder;

    private String pathSpec;

    private EnumSet<DispatcherType> dispatches;

    public FilterReference(Filter filter, String pathSpec, EnumSet<DispatcherType> dispatches) {
        this( new FilterHolder( filter ), pathSpec, dispatches );
    }

    public FilterReference(Class<? extends Filter> filter, String pathSpec, EnumSet<DispatcherType> dispatches) {
        this( new FilterHolder( filter ), pathSpec, dispatches );
    }

    public FilterReference(FilterHolder filterHolder, String pathSpec, EnumSet<DispatcherType> dispatches) {
        this.filterHolder = filterHolder;
        this.pathSpec = pathSpec;
        this.dispatches = dispatches;
    }

    public FilterHolder getFilterHolder() {
        return filterHolder;
    }

    public String getPathSpec() {
        return pathSpec;
    }

    public EnumSet<DispatcherType> getDispatches() {
        return dispatches;
    }

}
