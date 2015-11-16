package com.spinn3r.artemis.network.builder.settings;

import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Settings {

    private String name;

    private int priority;

    private String regex;

    private Boolean followContentRedirects = null;

    private Pattern pattern;

    public Settings(String name, int priority, String regex, Boolean followContentRedirects) {
        this.name = name;
        this.priority = priority;
        this.regex = regex;
        this.followContentRedirects = followContentRedirects;
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

    public Boolean getFollowContentRedirects() {
        return followContentRedirects;
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
        return "Settings{" +
                 "name='" + name + '\'' +
                 ", priority=" + priority +
                 ", regex='" + regex + '\'' +
                 ", followContentRedirects=" + followContentRedirects +
                 '}';
    }

}
