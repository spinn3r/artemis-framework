package com.spinn3r.artemis.util.iterators.batched;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.util.iterators.ThrowingIterator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public interface BatchedThrowingIterator<T,E extends Throwable> {

    boolean hasNext() throws E;

    ImmutableList<T> next() throws E;

    /**
     * Convert this to a simpler iterator interface that automatically handles
     * paging for you.  This is easier to use but you won't have any control
     * around iterating between pages.
     */
    default ThrowingIterator<T,E> toAutoIterator() {

        return new ThrowingIterator<T, E>() {

            Iterator<T> entryIterator = new ArrayList<T>(0).iterator();

            @Override
            public boolean hasNext() throws E {

                while(! entryIterator.hasNext()) {

                    if(BatchedThrowingIterator.this.hasNext()) {
                        entryIterator = BatchedThrowingIterator.this.next().iterator();
                    } else {
                        return false;
                    }

                }

                return true;

            }

            @Override
            public T next() throws E {
                return entryIterator.next();
            }

        };

    }

}

