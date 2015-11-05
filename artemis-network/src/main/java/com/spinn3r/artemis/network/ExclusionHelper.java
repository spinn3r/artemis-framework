package com.spinn3r.artemis.network;

import com.spinn3r.log5j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: URLResourceRequest.java 159213 2005-03-27 23:32:01Z burton $
 */
public class ExclusionHelper {

    private static Logger log = Logger.getLogger();

    private List<ExclusionProvider> providers = new ArrayList<>();

    public ExclusionHelper() {

    }

    public ExclusionHelper(List<ExclusionProvider> providers) {
        this.providers = providers;
    }

    public boolean isExcluded( String link ) {

        for( ExclusionProvider p : providers ) {

            if ( p.isExcluded( link ) ) {
                return true;
            }

        }

        return false;

    }

}