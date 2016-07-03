package com.spinn3r.artemis.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Test;

import static com.spinn3r.artemis.init.Services.ref;
import static org.junit.Assert.assertEquals;

public class TestConfig {

    @Test
    public void test1() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();

        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                    .build();

        launcher.launch( ref( Service1.class ) );

    }

    @Test
    public void test2() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();
        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                     .build();

        launcher.launch( ref( Service2.class ) );

    }

    @Test
    public void test3() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();
        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                     .build();

        launcher.launch( ref( Service3.class ) );

        launcher.getAdvertised().require( ServiceConfig.class );

    }

    @Test(expected = RuntimeException.class)
    public void test4() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();
        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                     .build();

        launcher.launch( ref( Service4.class ) );

    }

    @Test
    public void test5() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();
        Launcher launcher = Launcher.forConfigLoader( configLoader )
                                     .build();

        launcher.launch( ref( Service5.class ) );

        //assertEquals( "bar",
        //              launcher.getServices().find( Service5.class ).getServiceConfig().getMember() );

        ServiceConfig config = launcher.getAdvertised().require( ServiceConfig.class );

        assertEquals( "bar", config.getMember() );

        ConfigReader configReader = new ConfigReader( launcher.advertised );

        assertEquals( "{\n" +
                        "  \"member\" : \"bar\"\n" +
                        "}",
                      configReader.read( launcher.getServices().get( 0 ) ) );

    }


}

class Service1 extends BaseService {

}

@Config( path="service2.conf",
         required = false,
         implementation = ServiceConfig.class )
class Service2 extends BaseService {


}

@Config( path = "service3.conf",
         required = true,
         implementation = ServiceConfig.class )
class Service3 extends BaseService {


}

@Config( path = "asdfasdfasdf.conf",
         required = true,
         implementation = ServiceConfig.class )
class Service4 extends BaseService {


}

@Config( path = "service5.conf",
         required = true,
         implementation = ServiceConfig.class )
class Service5 extends BaseService {

    private ServiceConfig serviceConfig;

    @Inject
    Service5(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

}

class ServiceConfig {

    private String member = "member";

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

}
