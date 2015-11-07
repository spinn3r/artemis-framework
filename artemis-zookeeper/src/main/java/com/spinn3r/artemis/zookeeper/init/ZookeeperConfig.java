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

    public ZookeeperConfig( @JsonProperty( "servers" ) List<String> servers,
                            @JsonProperty( "connectTimeout" ) int connectTimeout,
                            @JsonProperty( "sessionTimeout" ) int sessionTimeout) {
        this.servers = servers;
        this.connectTimeout = connectTimeout;
        this.sessionTimeout = sessionTimeout;
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

    @Override
    public String toString() {
        return "ZookeeperConfig{" +
                 "servers=" + servers +
                 ", connectTimeout=" + connectTimeout +
                 ", sessionTimeout=" + sessionTimeout +
                 '}';
    }

}
