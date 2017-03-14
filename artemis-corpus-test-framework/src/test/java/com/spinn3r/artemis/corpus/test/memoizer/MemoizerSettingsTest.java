package com.spinn3r.artemis.corpus.test.memoizer;

import org.junit.Test;

import static org.junit.Assert.*;

public class MemoizerSettingsTest {

    @Test
    public void testProperties() throws Exception {

        MemoizerSettings memoizerSettings = MemoizerSettings.SYSTEM_PROPERTIES;

        assertFalse(memoizerSettings.isUpdateMode(getClass()));

        assertEquals("Use -Dcom.spinn3r.artemis.corpus.test.memoizer.MemoizerSettingsTest:update-enabled=true to force update", memoizerSettings.failureMessage(getClass()));

        System.setProperty("com.spinn3r.artemis.corpus.test.memoizer.MemoizerSettingsTest:update-enabled", "true");

        assertTrue(memoizerSettings.isUpdateMode(getClass()));

    }

}