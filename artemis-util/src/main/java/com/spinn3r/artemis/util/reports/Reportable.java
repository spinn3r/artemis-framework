package com.spinn3r.artemis.util.reports;

/**
 * <p>
 * Implements report() methods which will reveal the state of the object
 * in an ASCII text format suitable for human readability, testing, etc.
 * </p>
 *
 * <p>
 * Should normally not be used in a production capacity except when building
 * admin tools.
 * </p>
 */
public interface Reportable {

    String report();

}
