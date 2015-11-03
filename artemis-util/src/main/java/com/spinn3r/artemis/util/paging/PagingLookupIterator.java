package com.spinn3r.artemis.util.paging;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class PagingLookupIterator<K,V> implements Iterator<KeyValue<K,V>> {

    private final Pager<K,V> pager;

    private final PagerCallback<K,V> pagerCallback;

    private final Iterator<List<K>> inputPagesIterator;

    private Iterator<KeyValue<K,V>> pageKeyValueIterator = createEmptyIterator();

    public PagingLookupIterator(Pager<K, V> pager, PagerCallback<K,V> pagerCallback, Iterator<List<K>> inputPagesIterator) {
        this.pager = pager;
        this.pagerCallback = pagerCallback;
        this.inputPagesIterator = inputPagesIterator;
    }

    @Override
    public boolean hasNext() {

        while( ! pageKeyValueIterator.hasNext() && inputPagesIterator.hasNext() ) {

            Collection<KeyValue<K, V>> page = pager.page( inputPagesIterator.next() );
            pageKeyValueIterator = page.iterator();
            pagerCallback.onPage( page );

        }

        return pageKeyValueIterator.hasNext();

    }

    @Override
    public KeyValue<K, V> next() {
        return pageKeyValueIterator.next();
    }

    private Iterator<KeyValue<K,V>> createEmptyIterator() {

        List<KeyValue<K,V>> list = Lists.newArrayList();
        return list.iterator();

    }

}
