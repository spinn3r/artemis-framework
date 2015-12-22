package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * A HitIndex contains a histograph of entries by key.
 *
 * Like a HitMap but supports generic keys.
 */
public class HitIndex<T extends Comparable<T>> {

    // TODO: decouple the reporting and dumping from the actual storage of the hits.

    private String name = null;

    public int doc_nr = 0;

    private Map<T,Entry<T>> delegate;

    public HitIndex() {
        this( "hitindex" );
    }

    public HitIndex(String name) {
        this( 1000, name );
    }

    public HitIndex(int size, String name) {
        this.delegate = new HashMap<>( size );
        this.name = name;
    }

    public void registerHit( T v ) {
        registerHit( v, 1 );
    }

    public void registerHit( T v, int increase ) {

        Entry<T> t = delegate.get( v );
        if ( t == null ) {
            t = new Entry<>( v );
            delegate.put( v, t );
        }

        t.inc( increase );

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

    protected Collection<Entry<T>> values() {
        return delegate.values();
    }

    protected void remove( T key ) {
        delegate.remove( key );
    }

    /**
     * After we've parsed out a training set we should merge it with the main
     * map.
     */
    public void merge( HitIndex<T> map ) {

        ++doc_nr;

        for( T key : map.keySet() ) {

            Entry<T> source = map.get( key );

            Entry<T> target = get( key );

            if ( target == null ) {

                put( key, source );

            } else {
                target.setCount( target.getCount() + source.getCount() );
            }

        }


    }

    public void init() {

        //assign positions
        TreeSet<Entry<T>> set = new TreeSet<>();
        set.addAll( values() );

        Iterator<Entry<T>> it = set.iterator();

        int p = 1;
        while( it.hasNext() ) {
            Entry<T> t = it.next();
            t.position = p;
            ++p;

        }

        //trim unnecessary terms
        trim();

    }

    /**
     * Trim words from this category which are irrelevant and would just take up
     * memory.  Usually when reference from less than the total set of documents.
     */
    public void trim() {

        List<Entry<T>> entries = Lists.newArrayList( values() );
        Collections.sort( entries, new Comparator<Entry<T>>() {
            @Override
            public int compare(Entry<T> t1, Entry<T> t2) {
                if ( t1.count > t2.count ) {
                    return -1;
                } else if ( t1.count < t2.count ) {
                    return 1;
                }

                return 0;
            }
        } );

        delegate.clear();

        for (Entry<T> entry : entries) {
            delegate.put( entry.key, entry );
        }

//        Entry<T> pointer = new Entry<T>() {
//
//            @Override
//            public int compareTo(Entry<T> t1) {
//
//
//            }
//
//        };
//

//
//        pointer.count = doc_nr + 1;
//        pointer.data = "";
//
//        System.out.println( "Trimming after: " + pointer.count );
//
//        TreeSet<Entry<T>> set = new TreeSet<>( values() );
//
//        SortedSet<Entry<T>> headset = set.headSet( pointer );
//
//        Iterator<Entry<T>> it = headset.iterator();
//
//        int count = 0;
//        while( it.hasNext() ) {
//
//            Entry<T> t = it.next();
//
//            System.out.println( "Removing: " + t.data + " - " + t.position + " - " + t.count );
//
//            remove( t.data );
//            ++count;
//
//        }
//
//        System.out.println( "Trimmed: " + count );

    }

    public List<T> read() {

        Set<Entry<T>> set = new TreeSet<>( values() );

        List<T> result = new ArrayList<>();

        for( Entry<T> t : set ) {
            result.add( t.key );
        }

        return result;

    }

    public List<T> read( int limit ) {

        List<T> result = read();

        if( result.size() > limit ) {
            result = CollectionUtils.head( result, limit );
        }

        return result;

    }

    public void dump() {
        dump( 50 );
    }

    public void dump( int limit ) {

        System.err.println( "hitindex: " + name );

        TreeSet<Entry<T>> set = new TreeSet<>( values() );

        int count = 0;

        for( Entry<T> t : set ) {
            System.err.println( t );
            ++count;

            if ( count == limit)
                break;

        }

    }

    public void report() {
        report( 50 );
    }

    public void report( int max ) {

        System.out.println( "hitindex: " + name );

        TreeSet<Entry<T>> set = new TreeSet<>();
        set.addAll( values() );

    }

    /**
     * return
     *
     * @return
     */
    public int rangeCount( int minInclusive, long maxInclusive ) {

        int count = 0;

        for (Entry<T> entry : delegate.values()) {

            if ( entry.getCount() >= minInclusive && entry.getCount() <= maxInclusive ) {
                ++count;
            }

        }

        return count;

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

        public void setCount( int count ) {
            this.count = count;
        }

        public void inc() {
            count++;
        }

        public void inc(int increase) {
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
