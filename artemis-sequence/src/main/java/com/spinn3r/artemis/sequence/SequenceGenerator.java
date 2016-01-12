package com.spinn3r.artemis.sequence;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.sequence.SequenceReference;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * This is a distributed, high performance, collision free, and fault tolerant
 * sequence generator.
 *
 * It hands out 64 bit values based on current unix system time, a cluster-wide
 * global writer ID (every thread that needs to write needs a unique ID), and a
 * local sequence value that's incremented on every generation.
 *
 * The format is:
 * [<----------------- 64 bits -------------------->]
 * [ time stamp ][     writer    ][ sequence value  ]
 * [  36 bits*  ][    14 bits    ][     14 bits     ]
 *
 *  Due to the way the shifting is done for the time stamp only ~34.1 bits
 *  are used.
 *
 * This provides totally independent local machine operation only requiring
 * coordination during startup.
 *
 * It uses cluster-wide course grained locks to ensure that it doesn't give
 * out the same range multiple times to multiple clients.
 *
 * Once the client has a lock it can then allocate a separate 2^14 local lock
 * identifier.  The system will block if it runs out of 2^14 local locks within
 * a one second interval.
 *
 */
@Singleton
public class SequenceGenerator {

    private static Logger log = Logger.getLogger();

    /**
     * Compute the maximum sequence value without any loss of precision (9223372036099999999).
     */
    public static final long MAX_VALUE = (Long.MAX_VALUE / SequenceReference.PADDING) * SequenceReference.PADDING + 99999999L;

    private static final int ID_WIDTH = 14;

    private static final int SEQUENCE_WIDTH = 14;

    /**
     * Max value of the sequences or (2^14) ...
     */
    public static final long MAX_SEQUENCE =
      (long)Math.pow( (double)2, (double)SEQUENCE_WIDTH );

    private static final int MAX_ID =
      (int)Math.pow( (double)2, (double)ID_WIDTH );

    /**
     * Time in ms to sleep if we exhaust our sequence.
     */
    public static long SLEEP_TIME = 100;

    /**
     * Lock used to prevent thread corruption.
     */
    private Object MUTEX = new Object();

    ///// state vars ////

    private long sequence = 0;
    private long lastRollover;

    /**
     * Total number of issued sequence timestamps.
     */
    private long issued = 0;

    private final Clock clock;

    private final Provider<GlobalMutex> globalMutexProvider;

    @Inject
    public SequenceGenerator(Clock clock, Provider<GlobalMutex> globalMutexProvider) {
        this.clock = clock;
        this.globalMutexProvider = globalMutexProvider;
        this.lastRollover = getTime();
    }

    //////// public interface /////////

    /**
     * Compute the next sequence identifier.
     */
    public SequenceReference next () {

        try {

            GlobalMutex globalMutex = globalMutexProvider.get();

            long writerId = globalMutex.getValue();

            long res = -1;

            synchronized( MUTEX ) {

                ++sequence;

                while ( true ) {

                    long now = getTime();

                    if ( now > lastRollover ) {
                        sequence     = 0;
                        lastRollover = now;
                    }

                    if ( sequence < MAX_SEQUENCE ) {
                        break;
                    } else {

                        log.warn( "Sequence generator sleeping for %,d for sequence = %s, generator id = %s, last rollover = %s, issued=%s",
                                  SLEEP_TIME, sequence, writerId, lastRollover, issued );

                        doSleep( SLEEP_TIME );

                    }

                }

                res = ( lastRollover  * SequenceReference.PADDING ) + writerId + sequence;
                ++issued;
            }

            return new SequenceReference( res );

        } catch (GlobalMutexExpiredException e) {
            throw new RuntimeException( e );
        }

    }

    /////// private interface ///////

    private static void doSleep ( long sleep_time ) {
        Uninterruptibles.sleepUninterruptibly( sleep_time, TimeUnit.MILLISECONDS );
    }

    private long getTime () {
        return clock.currentTimeMillis() / SequenceReference.RESOLUTION;
    }

}


