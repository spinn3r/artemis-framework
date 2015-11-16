package com.spinn3r.artemis.network.builder.settings.requests;

import com.spinn3r.artemis.network.init.RequestSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class RequestSettingsReference {

    private final String name;

    private final int priority;

    private final String regex;

    private final RequestSettings requestSettings;

    private Pattern pattern;

    public RequestSettingsReference(String name, RequestSettings requestSettings) {
        this.name = name;
        this.priority = requestSettings.getPriority();
        this.regex = requestSettings.getRegex();
        this.requestSettings = requestSettings;
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

    public RequestSettings getRequestSettings() {
        return requestSettings;
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
                 ", requestSettings=" + requestSettings +
                 '}';
    }

}
