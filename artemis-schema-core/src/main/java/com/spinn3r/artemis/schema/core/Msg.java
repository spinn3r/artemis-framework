package com.spinn3r.artemis.schema.core;

/**
 * A message (msg) that can be sent over the wire and serialized and deserialized
 * to JSON.  This is named as Msg instead of Message to avoid confusion with JMS
 * Message objects.
 */
public interface Msg {

    String toJSON();

    @Override
    String toString();

}
