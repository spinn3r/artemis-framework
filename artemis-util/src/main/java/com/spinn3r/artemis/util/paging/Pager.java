package com.spinn3r.artemis.util.paging;

import java.util.Collection;

/**
 *
 */
public interface Pager<K,V> {

    Collection<KeyValue<K,V>> page( Collection<K> keys );

}
