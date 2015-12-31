package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.resource_finder.references.ResourceReference;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ClasspathResources {

    public static ImmutableList<String> toPaths(Collection<ResourceReference> resourceReferences ) {

        List<String> result = Lists.newArrayList();

        for (ResourceReference resourceReference : resourceReferences) {
            result.add( resourceReference.getPath() );
        }

        return ImmutableList.copyOf( result );

    }

}
