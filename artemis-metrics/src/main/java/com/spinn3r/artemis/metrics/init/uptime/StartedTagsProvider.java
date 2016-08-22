package com.spinn3r.artemis.metrics.init.uptime;

import java.util.List;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.metrics.tags.SimpleTagsProvider;
import com.spinn3r.artemis.time.Started;
import com.spinn3r.metrics.kairosdb.Tag;

/**
 *
 */
public class StartedTagsProvider extends SimpleTagsProvider<Started> {

    StartedTagsProvider() {
        setDenormalizedOnly(false);
    }
    
    @Override
    public List<Tag> getTags( Started started ) {

        List<Tag> tags = Lists.newArrayList();

        return tags;

    }

}
