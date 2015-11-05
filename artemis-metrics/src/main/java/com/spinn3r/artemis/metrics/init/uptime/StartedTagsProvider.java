package com.spinn3r.artemis.metrics.init.uptime;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.metrics.tags.SimpleTagsProvider;
import com.spinn3r.artemis.time.Started;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.List;

/**
 *
 */
public class StartedTagsProvider extends SimpleTagsProvider<Started> {

    @Override
    public List<Tag> getTags( Started started ) {

        List<Tag> tags = Lists.newArrayList();

        return tags;

    }

}
