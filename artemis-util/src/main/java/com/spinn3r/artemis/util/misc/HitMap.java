package com.spinn3r.artemis.util.misc;

import java.util.*;

/**
 * <p>
 * A HitMap contains a histograph of entries by key.
 *</p>
 *
 * <p>
 * While this isn't fully deprecated, you probably want to use a HitIndex
 * instead.
 * </p>
 */
public class HitMap {

    private String name = null;

    public int doc_nr = 0;

    private Map<String,Entry> delegate;

    public HitMap() {
        this( "hitmap" );
    }

    public HitMap( String name ) {
        this( 1000, name );
    }

    public HitMap( int size, String name ) {
        this.delegate = new HashMap<>( size );
        this.name = name;
    }

    public void registerHit( String v ) {
        registerHit( v, 1 );
    }

    public void registerHit( String v, int increase ) {

        Entry t = delegate.get( v );
        if ( t == null ) {
            t = new Entry( v );
            delegate.put( v, t );
        }

        t.inc( increase );

    }

    public Set<String> keySet() {
        return delegate.keySet();
    }

    public Entry get( String key ) {
        return delegate.get( key );
    }

    public int size() {
        return delegate.size();
    }

    protected void put( String key, Entry value ) {
        delegate.put( key, value );
    }

    protected Collection<Entry> values() {
        return delegate.values();
    }

    protected void remove( String key ) {
        delegate.remove( key );
    }

    /**
     * After we've parsed out a training set we should merge it with the main
     * map.
     */
    public void merge( HitMap map ) {

        ++doc_nr;

        for( String key : map.keySet() ) {

            Entry source = map.get( key );

            Entry target = get( key );

            if ( target == null ) {

                put( key, source );

            } else {
                target.setCount( target.getCount() + source.getCount() );
            }

        }


    }

    public void init() {

        //assign positions
        TreeSet<Entry> set = new TreeSet<>();
        set.addAll( values() );

        Iterator<Entry> it = set.iterator();

        int p = 1;
        while( it.hasNext() ) {
            Entry t = it.next();
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

        Entry pointer = new Entry() {

            @Override
            public int compareTo(Entry t1) {

                if ( t1.count > count ) {
                    return -1;
                } else if ( t1.count < count ) {
                    return 1;
                }

                return 0;

            }

        };

        pointer.count = doc_nr + 1;
        pointer.data = "";

        System.out.println( "Trimming after: " + pointer.count );

        TreeSet<Entry> set = new TreeSet<>( values() );

        SortedSet<Entry> headset = set.headSet( pointer );

        Iterator<Entry> it = headset.iterator();

        int count = 0;
        while( it.hasNext() ) {

            Entry t = it.next();

            System.out.println( "Removing: " + t.data + " - " + t.position + " - " + t.count );

            remove( t.data );
            ++count;

        }

        System.out.println( "Trimmed: " + count );

    }

    public List<String> read() {

        Set<Entry> set = new TreeSet<>( values() );

        List<String> result = new ArrayList<>();

        for( Entry t : set ) {
            result.add( t.data );
        }

        return result;

    }

    public List<String> read( int limit ) {

        List<String> result = read();

        if( result.size() > limit ) {
            result = result.subList( 0, limit - 1 );
        }

        return result;

    }

    public void dump() {
        dump( 50 );
    }

    public void dump( int limit ) {

        System.err.println( "hitmap: " + name );

        TreeSet<Entry> set = new TreeSet<>( values() );

        int count = 0;

        for( Entry t : set ) {
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

        System.out.println( "hitmap: " + name );

        TreeSet<Entry> set = new TreeSet<>();
        set.addAll( values() );

    }

    public class Entry implements Comparable<Entry> {

        protected int count = 0;

        protected String data = null;

        protected int position = -1;

        public Entry() {}

        public Entry(String data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object e1) {

            if ( ! (e1 instanceof Entry) )
                return false;

            Entry n1 = (Entry)e1;
            return n1.data.equals( data );

        }

        public int getSize() {
            return data.length();
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

        @Override
        public String toString() {
            return data + " : " + count;

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
        public int compareTo(Entry e1) {

            if ( e1 == null )
                return -1;

            if ( e1.data == null )
                return -1;

            if ( e1.count > count ) {
                return 1;
            } else if ( e1.count < count ) {
                return -1;
            }

            return e1.data.compareTo( data );

        }

    }

}
