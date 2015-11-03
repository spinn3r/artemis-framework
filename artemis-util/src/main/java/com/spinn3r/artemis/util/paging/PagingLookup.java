package com.spinn3r.artemis.util.paging;

import com.spinn3r.artemis.util.misc.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class PagingLookup<K,V> {

    private final int pageSize;

    private final Pager<K,V> pager;

    private final PagerCallback<K,V> pagerCallback;

    public PagingLookup(Pager<K,V> pager) {
        this( 500, pager );
    }

    public PagingLookup(int pageSize, Pager<K,V> pager) {
        this( pageSize, pager, (Collection<KeyValue<K,V>> page) -> {});
    }

    public PagingLookup(int pageSize, Pager<K, V> pager, PagerCallback<K, V> pagerCallback) {
        this.pageSize = pageSize;
        this.pager = pager;
        this.pagerCallback = pagerCallback;
    }

    public Iterator<KeyValue<K,V>> lookup( Collection<K> input ) {

        List<List<K>> inputPages = CollectionUtils.group( input, pageSize );

        return new PagingLookupIterator<>( pager, pagerCallback, inputPages.iterator() );

    }

}
