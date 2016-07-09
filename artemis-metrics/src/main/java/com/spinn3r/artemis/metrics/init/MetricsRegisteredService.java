package com.spinn3r.artemis.metrics.init;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.metrics.kairosdb.TaggedMetrics;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class MetricsRegisteredService extends BaseService {

    private final TaggedMetrics taggedMetrics;

    @Inject
    MetricsRegisteredService(TaggedMetrics taggedMetrics) {
        this.taggedMetrics = taggedMetrics;
    }

    @Override
    public void start() throws Exception {

        List<String> metricNames
          = taggedMetrics
              .getMetricRegistry()
              .getMetrics()
              .keySet()
              .stream()
              .sorted()
              .collect(Collectors.toList());

        info( "The following %,d metrics are registered: \n%s",
              metricNames.size(),
              Joiner.on("  \n").join(metricNames) );

    }

}
