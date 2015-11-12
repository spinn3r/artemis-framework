package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import java.util.List;

/**
 *
 */
public class ControllerArgs {

    @Parameter(names = { "--sourceIndexes" }, description = "Source indexes to read from.", required = true,  variableArity = true)
    private List<String> sourceIndexes;

    @Parameter(names = { "--targetIndex" }, description = "Target index to write to.", required = false)
    private String targetIndex = null;

    @Parameter(names = { "--optimize" }, description = "True if we should optimize the resulting target index.", required = false)
    private boolean optimize = true;

    @Parameter(names = "--help", help = true)
    private boolean help = false;

    public ControllerArgs() {
    }

    public ControllerArgs(List<String> sourceIndexes, String targetIndex) {
        this.sourceIndexes = sourceIndexes;
        this.targetIndex = targetIndex;
    }

    public ControllerArgs(List<String> sourceIndexes, String targetIndex, boolean optimize) {
        this.sourceIndexes = sourceIndexes;
        this.targetIndex = targetIndex;
        this.optimize = optimize;
    }

    public List<String> getSourceIndexes() {
        return sourceIndexes;
    }

    public String getTargetIndex() {
        return targetIndex;
    }

    public boolean getOptimize() {
        return optimize;
    }

    public boolean getHelp() {
        return help;
    }

    @Override
    public String toString() {
        return "ControllerArgs{" +
                 "sourceIndexes=" + sourceIndexes +
                 ", targetIndex='" + targetIndex + '\'' +
                 ", optimize=" + optimize +
                 ", help=" + help +
                 '}';
    }

}
