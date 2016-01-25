package com.spinn3r.artemis.zookeeper.embedded;

/**
 *
 */
public class EmbeddedZookeeperPort {

    private final int port;

    public EmbeddedZookeeperPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "EmbeddedZookeeperPort{" +
                 "port=" + port +
                 '}';
    }

}
