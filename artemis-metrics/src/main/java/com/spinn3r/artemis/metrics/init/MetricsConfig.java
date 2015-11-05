package com.spinn3r.artemis.metrics.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

import java.util.*;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricsConfig {

    private boolean enabled;

    private String endpoint;

    private Reporter reporter;

    private List<Reporter> reporters = Lists.newArrayList();

    private SparseMetricsIndex sparseMetricsIndex = new SparseMetricsIndex();

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * the REST endpoint for KairosDB queries
     *
     * @return
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @Deprecated Use getReporters so that we can use multiple reporters at once
     * @return
     */
    public Reporter getReporter() {
        return reporter;
    }

    public List<Reporter> getReporters() {
        return reporters;
    }

    public SparseMetricsIndex getSparseMetricsIndex() {
        return sparseMetricsIndex;
    }

    @Override
    public String toString() {
        return "MetricsConfig{" +
                 "enabled=" + enabled +
                 ", reporter=" + reporter +
                 ", sparseMetricsIndex=" + sparseMetricsIndex +
                 '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reporter {

        private int interval;

        private String hostname;

        private int port;

        private String type;

        public Reporter() { }

        public int getInterval() {
            return interval;
        }

        public String getHostname() {
            return hostname;
        }

        public int getPort() {
            return port;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Reporter{" +
                     "interval=" + interval +
                     ", hostname='" + hostname + '\'' +
                     ", port=" + port +
                     ", type='" + type + '\'' +
                     '}';
        }

    }

    public static class SparseMetricsIndex extends TreeMap<String,SparseMetricTags> {

    }

    public static class SparseMetricTags extends TreeMap<String,SparseMetricTagValues> {
    }

    public static class SparseMetricTagValues extends TreeSet<String> {

    }

}
