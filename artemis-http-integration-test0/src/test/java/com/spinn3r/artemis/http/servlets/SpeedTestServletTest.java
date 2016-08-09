package com.spinn3r.artemis.http.servlets;

import com.spinn3r.artemis.http.init.BasicWebserverReferencesService;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.init.BaseLauncherTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class SpeedTestServletTest extends BaseLauncherTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(BasicWebserverReferencesService.class,
                    WebserverService.class );
    }

    @Test
    public void testDoGet() throws Exception {



    }

}