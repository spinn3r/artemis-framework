package com.spinn3r.artemis.tracepoints;

import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Role;
import com.spinn3r.artemis.init.advertisements.Version;

/**
 *
 */
public class HostInfo {

    private final Hostname hostname;

    private final Role role;

    private final Version version;

    public HostInfo(Hostname hostname, Role role, Version version) {
        this.hostname = hostname;
        this.role = role;
        this.version = version;
    }

    public Hostname getHostname() {
        return hostname;
    }

    public Role getRole() {
        return role;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "HostInfo{" +
                 "hostname=" + hostname +
                 ", role=" + role +
                 ", version=" + version +
                 '}';
    }

}
