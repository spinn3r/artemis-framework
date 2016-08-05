package com.spinn3r.artemis.init.cache;

import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import org.junit.Assert;
import org.junit.Test;

public class ServiceCacheTest {

    @Test
    public void testCachedServices() throws Exception {

        Launcher launcher = Launcher.newBuilder().build();

        launcher.launch(new ServiceReferences().add(FooService.class));

        Assert.assertEquals(0, launcher.getServiceCache().hits());

        launcher = Launcher.newBuilder().build();

        launcher.launch(new ServiceReferences().add(FooService.class));

        Assert.assertEquals(1, launcher.getServiceCache().hits());

    }

    static class FooService extends BaseService {

        AtomicReferenceProvider<Foo> fooProvider = new AtomicReferenceProvider<>();

        @Override
        public void init() {
            provider(Foo.class, fooProvider);
        }

        @Override
        protected void configure() {
            bind(Foo.class).toProvider(fooProvider);
        }

        @Override
        public void start() throws Exception {
            fooProvider.set( cached(Foo.class, Foo::new));
        }

    }

    static class Foo {


    }

}