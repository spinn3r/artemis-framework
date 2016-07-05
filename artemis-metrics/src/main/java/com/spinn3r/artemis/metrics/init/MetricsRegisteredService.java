package com.spinn3r.artemis.metrics.init;

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

        info( "The following metrics are registered: ");

        for (String metricName : metricNames) {
            System.out.printf("  %s\n", metricName);
        }

    }

}
