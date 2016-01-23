package com.spinn3r.artemis.zookeeper.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZookeeperConfig {

    private List<String> servers = new ArrayList<>();

    private int connectTimeout;

    private int sessionTimeout;

    private String namespace = "artemis";

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
