package com.spinn3r.artemis.resource_finder.references;

import com.spinn3r.artemis.resource_finder.ResourceHolder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public abstract class ResourceReference {

    private final ResourceHolder resourceHolder;

    private final String path;

    public ResourceReference(ResourceHolder resourceHolder, String path) {
        this.resourceHolder = resourceHolder;
        this.path = path;
    }

    public ResourceHolder getResourceHolder() {
        return resourceHolder;
    }

    public String getPath() {
        return path;
    }

    public abstract InputStream getInputStream() throws IOException;

}
