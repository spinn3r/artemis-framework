package com.spinn3r.artemis.json;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public interface MsgSerializable extends Msg {

    default void write(OutputStream outputStream) throws IOException {
        outputStream.write(toJSON().getBytes(StandardCharsets.UTF_8));
    }

}
