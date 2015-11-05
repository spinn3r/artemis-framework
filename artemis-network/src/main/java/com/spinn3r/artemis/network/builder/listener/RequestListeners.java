package com.spinn3r.artemis.network.builder.listener;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry of all request listeners being used.
 */
public class RequestListeners implements Iterable<RequestListener> {

    private CopyOnWriteArrayList<RequestListener> listeners = new CopyOnWriteArrayList<>();

    public void add( RequestListener requestListener ) {
        listeners.add( requestListener );
    }

    @Override
    public Iterator<RequestListener> iterator() {
        return listeners.iterator();
    }

}
