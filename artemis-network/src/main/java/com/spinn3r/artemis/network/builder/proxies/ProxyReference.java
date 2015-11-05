package com.spinn3r.artemis.network.builder.proxies;

import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ProxyReference {

    private String name;

    private int priority;

    private String regex;

    private Proxy proxy;

    private Pattern pattern;

    public ProxyReference(String name, int priority, String regex, Proxy proxy) {
        this.name = name;
        this.priority = priority;
        this.regex = regex;
        this.proxy = proxy;
        this.pattern = Pattern.compile( regex );
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getRegex() {
        return regex;
    }

    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Return true if this proxy server supports the given link.
     * @param link
     * @return
     */
    public boolean supports( String link ) {

        Matcher matcher = pattern.matcher( link );
        return matcher.find();

    }

    @Override
    public String toString() {
        return "ProxyReference{" +
                 "name='" + name + '\'' +
                 ", priority=" + priority +
                 ", regex='" + regex + '\'' +
                 ", proxy=" + proxy +
                 '}';
    }

}
