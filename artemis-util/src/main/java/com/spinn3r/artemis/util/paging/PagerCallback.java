package com.spinn3r.artemis.util.paging;

import java.util.Collection;

/**
 *
 */
public interface PagerCallback<K,V> {

    /**
     * Called on each page of a pager.  This way we can perform actions  like
     * optionally log each page or compute the number of items, RAM, etc.
     * @param page
     */
    void onPage( Collection<KeyValue<K,V>> page );

}
