package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.internal.Sets;

import java.util.Set;

/**
 *
 */
public class SetArgs {

    @Parameter(names = { "--langs" }, description = "True if we should optimize the resulting target index.", required = true, variableArity = true)
    protected Set<String> langs = Sets.newHashSet();

    public Set<String> getLangs() {
        return langs;
    }

    @Override
    public String toString() {
        return "SetArgs{" +
                 "langs=" + langs +
                 '}';
    }
}
