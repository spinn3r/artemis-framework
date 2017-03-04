package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.init.LauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequestMethods;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeiboCookiesTest extends LauncherTest {


    @Override
    @Before
    public void setUp() throws Exception {

        setServiceReferences(ImmutableList.of(DirectNetworkService.class));

        super.setUp();
    }

    @Test
    public void testWeiboCookies() throws Exception {

    }

}