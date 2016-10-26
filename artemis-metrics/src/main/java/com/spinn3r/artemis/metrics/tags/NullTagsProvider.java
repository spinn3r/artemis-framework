package com.spinn3r.artemis.metrics.tags;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.List;

/**
 *
 */
@Singleton
public class NullTagsProvider<T> extends SimpleTagsProvider<T> {

    @Override
    public List<Tag> getTags(T value) {
        return Lists.newArrayList();
    }

}
