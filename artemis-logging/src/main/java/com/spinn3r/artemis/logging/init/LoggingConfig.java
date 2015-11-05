package com.spinn3r.artemis.logging.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoggingConfig {

    private String conf;

    private String dir;

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "LoggingConfig{" +
                 "conf='" + conf + '\'' +
                 ", dir='" + dir + '\'' +
                 '}';
    }

}
