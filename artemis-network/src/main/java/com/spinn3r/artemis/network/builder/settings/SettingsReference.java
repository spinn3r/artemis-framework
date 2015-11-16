package com.spinn3r.artemis.network.builder.settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SettingsReference {

    private String name;

    private int priority;

    private String regex;

    private Pattern pattern;

    public SettingsReference(String name, int priority, String regex) {
        this.name = name;
        this.priority = priority;
        this.regex = regex;
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
        return "SettingsReference{" +
                 "name='" + name + '\'' +
                 ", priority=" + priority +
                 ", regex='" + regex + '\'' +
                 ", pattern=" + pattern +
                 '}';
    }

}
