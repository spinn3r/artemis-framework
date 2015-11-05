package com.spinn3r.artemis.test.proxy;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import org.apache.commons.proxy.Interceptor;
import org.apache.commons.proxy.Invocation;
import org.apache.commons.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 */
public class Test {

    public static void main(String[] args) {

        final Foo target = new Foo();

        InvocationHandler invocationHandler = new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke( target, args );
            }
        };

        IFoo foo = Reflection.newProxy( IFoo.class, invocationHandler );

        System.out.printf( "%s\n", foo.testMethod() );

    }
}
