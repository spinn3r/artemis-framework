package com.spinn3r.artemis.network.validators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 */
public class DefaultHttpResponseValidators implements HttpResponseValidators {

    private final List<HttpResponseValidator> httpResponseValidators  = Lists.newCopyOnWriteArrayList();

    @Override
    public ImmutableList<HttpResponseValidator> getResponseValidators() {
        return ImmutableList.copyOf( httpResponseValidators );
    }

    @Override
    public void add(HttpResponseValidator httpResponseValidator) {
        httpResponseValidators.add( httpResponseValidator );
    }

}
