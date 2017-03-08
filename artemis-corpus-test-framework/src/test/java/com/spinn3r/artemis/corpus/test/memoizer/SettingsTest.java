package com.spinn3r.artemis.corpus.test.memoizer;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class SettingsTest {

    @Test
    public void testProperties() throws Exception {

        Settings settings = Settings.SYSTEM_PROPERTIES;

        assertFalse(settings.isUpdateMode(getClass()));

        assertEquals("Use -Dcom.spinn3r.artemis.corpus.test.memoizer.SettingsTest:update-enabled=true to force update", settings.failureMessage(getClass()));

        System.setProperty("com.spinn3r.artemis.corpus.test.memoizer.SettingsTest:update-enabled", "true");

        assertTrue(settings.isUpdateMode(getClass()));

    }

}