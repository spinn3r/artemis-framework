package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 *
 */
@Parameters(separators = "=")
public class ExampleArgs {

    @Parameter(names = { "--optimize" }, description = "True if we should optimize the resulting target index.", required = false, arity = 1)
    protected boolean optimize = true;

    @Override
    public String toString() {
        return "ExampleArgs{" +
                 "optimize=" + optimize +
                 '}';
    }
}
