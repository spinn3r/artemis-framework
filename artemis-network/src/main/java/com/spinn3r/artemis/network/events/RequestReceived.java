package com.spinn3r.artemis.network.events;

/**
 *
 */
public class RequestReceived {

    final private String method;

    final private String resource;

    final private String address;

    final private long duration;

    final int response;

    public RequestReceived(String method, String resource, String address, long duration, int response) {
        this.method = method;
        this.resource = resource;
        this.address = address;
        this.duration = duration;
        this.response = response;
    }

    public String getMethod() {
        return method;
    }

    /**
     * The resource / URL (IE http://cnn.com)
     *
     * @return
     */
    public String getResource() {
        return resource;
    }

    /**
     * The IP address which requested this resource (171.31.213.22)
     * @return
     */
    public String getAddress() {
        return address;
    }

    public long getDuration() {
        return duration;
    }

    public int getResponse() {
        return response;
    }
}
