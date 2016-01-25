package com.spinn3r.artemis.zookeeper.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZookeeperConfig {

    private List<String> servers = new ArrayList<>();

    private int connectTimeout = 60_000;

    private int sessionTimeout = 120_000;

    private String namespace = "artemis";

    public ZookeeperConfig() {
    }

    public ZookeeperConfig(List<String> servers, int connectTimeout, int sessionTimeout, String namespace) {
        this.servers = servers;
        this.connectTimeout = connectTimeout;
        this.sessionTimeout = sessionTimeout;
        this.namespace = namespace;
    }

    public ZookeeperConfig(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getServers() {
        return servers;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public String toString() {
        return "ZookeeperConfig{" +
                 "servers=" + servers +
                 ", connectTimeout=" + connectTimeout +
                 ", sessionTimeout=" + sessionTimeout +
                 ", namespace='" + namespace + '\'' +
                 '}';
    }

}
