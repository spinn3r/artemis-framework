package com.spinn3r.artemis.network.builder.proxies;

import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class PrioritizedProxyReference extends ProxyReference {

    private final String name;

    private final int priority;

    private final String regex;

    private final Proxy proxy;

    private final Pattern pattern;

    public PrioritizedProxyReference( String name, int priority, String regex, String host, int port, Proxy proxy ) {
        super( host, port, proxy );
        this.name = name;
        this.priority = priority;
        this.regex = regex;
        this.pattern = Pattern.compile( regex );
        this.proxy = proxy;
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

    public Pattern getPattern() {
        return pattern;
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
        return "PrioritizedProxyReference{" +
                 "name='" + name + '\'' +
                 ", priority=" + priority +
                 ", regex='" + regex + '\'' +
                 ", proxy=" + proxy +
                 ", pattern=" + pattern +
                 "} " + super.toString();
    }

}
