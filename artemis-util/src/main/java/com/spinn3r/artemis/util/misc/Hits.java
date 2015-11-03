package com.spinn3r.artemis.util.misc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * Hits contains a histograph of entries by key.
 *
 * Like a HitMap but supports generic keys.
 */
public class Hits<T extends Comparable<T>> {

    private Map<T,Entry<T>> delegate;

    public Hits() {
        this( 1000 );
    }

    public Hits(int size) {
        this.delegate = new HashMap<>( size );
    }

    public void incr( T key ) {
        incr( key, 1 );
    }

    public void incr( T key, int increase ) {

        checkNotNull( key );

        Entry<T> t = delegate.get( key );
        if ( t == null ) {
            t = new Entry<>( key );
            delegate.put( key, t );
        }

        t.incr( increase );

    }

    public Set<T> keySet() {
        return delegate.keySet();
    }

    public Entry<T> get( T key ) {
        return delegate.get( key );
    }

    public int size() {
        return delegate.size();
    }

    protected void put( T key, Entry<T> value ) {
        delegate.put( key, value );
    }

    public Collection<Entry<T>> values() {
        return delegate.values();
    }

    protected void remove( T key ) {
        delegate.remove( key );
    }

    /**
     * Return the total hits across all keys.
     * @return
     */
    public int totalHits() {

        int sum = 0;

        for (Entry<T> entry : delegate.values()) {
            sum += entry.getCount();
        }

        return sum;

    }

    public class Entry<T extends Comparable<T>> implements Comparable<Entry<T>> {

        protected int count = 0;

        protected T key = null;

        protected int position = -1;

        public Entry() {}

        public Entry(T key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object e1) {

            if ( ! (e1 instanceof Entry) )
                return false;

            @SuppressWarnings({"unchecked"})
            Entry<T> n1 = (Entry<T>)e1;
            return n1.key.equals( key );

        }

        public int getSize() {
            return key.toString().length();
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public String toString() {
            return key + " : " + count;

        }

        public int getCount() {
            return count;
        }

        public T getKey() {
            return key;
        }

        protected void incr() {
            count++;
        }

        protected void incr(int increase) {
            count+=increase;
        }

        @Override
        public int compareTo(Entry<T> e1) {

            if ( e1 == null )
                return -1;

            if ( e1.key == null )
                return -1;

            if ( e1.count > count ) {
                return 1;
            } else if ( e1.count < count ) {
                return -1;
            }

            return e1.key.compareTo( key );

        }

    }

}
