package com.spinn3r.artemis.test.cassandra;

/**
 *
 */
public class EmbeddedCassandraConfig {

    private String keyspace = "blogindex";

    private int timeout = 240000;

    private int port = 9142;

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
