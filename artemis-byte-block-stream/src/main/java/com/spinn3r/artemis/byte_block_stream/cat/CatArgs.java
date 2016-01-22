package com.spinn3r.artemis.byte_block_stream.cat;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.spinn3r.artemis.jcommander.ArgsFormatter;

/**
 *
 */
@Parameters(separators = "=")
public class CatArgs {

    @Parameter(names = { "--path" }, description = "The directory to write files.", required = true)
    private String path = null;

    @Parameter(names = { "--help", "-help" }, help = true)
    protected boolean help = false;

    public String getPath() {
        return path;
    }

    public boolean getHelp() {
        return help;
    }

    @Override
    public String toString() {
        return "CatArgs{" +
                 "path='" + path + '\'' +
                 ", help=" + help +
                 '}';
    }

    public String format() {
        return ArgsFormatter.format( this );
    }

}
