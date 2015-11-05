package com.spinn3r.artemis.metrics.jvm.threads;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.metrics.tags.SimpleTagsProvider;
import com.spinn3r.artemis.metrics.gauge.GaugeProvider;
import com.spinn3r.metrics.kairosdb.Tag;

import java.util.List;

/**
 *
 */
public class ThreadMetaGaugeProvider extends SimpleTagsProvider<ThreadMeta> implements GaugeProvider<ThreadMeta> {

    @Override
    public String getKey(ThreadMeta value) {
        return value.toKey();
    }

    @Override
    public long getValue(ThreadMeta value) {
        return 1;
    }

    @Override
    public List<Tag> getTags(ThreadMeta value) {

        List<Tag> tags = Lists.newArrayList();
        return tags;

    }

}
