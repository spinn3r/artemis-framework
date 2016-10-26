package com.spinn3r.artemis.json;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TypedErrorMessageTest {


    @Test
    public void testBasicTypes() throws Exception {

        TypedErrorMessage typedErrorMessage = new TypedErrorMessage(true, "hello world", FakeErrorType.FAILURE_ON_LAUNCH);

        assertEquals("{\n" +
                       "  \"failed\" : true,\n" +
                       "  \"message\" : \"hello world\",\n" +
                       "  \"type\" : \"FAILURE_ON_LAUNCH\"\n" +
                       "}",
                     typedErrorMessage.toJSON());

    }

    enum FakeErrorType {

        FAILURE_ON_LAUNCH;

    }

}