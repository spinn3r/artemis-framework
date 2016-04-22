package com.spinn3r.artemis.sequence;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.sequence.SequenceReference;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static com.spinn3r.artemis.time.sequence.SequenceReference.*;

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
    // FIXME: I think this value is wrong too...
    public static final long MAX_VALUE = (Long.MAX_VALUE / GLOBAL_TIME_PADDING) * GLOBAL_TIME_PADDING + 99999999L;

    /**
     */
    public static final long MAX_SEQUENCE = 99999;

    public static final long MIN_WRITER_ID = 0;

    public static final long MAX_WRITER_ID = 9999;

    /**
     * Time in ms to sleep if we exhaust our sequence.
     */
    public static long SLEEP_TIME = 100;

    /**
     * Lock used to prevent thread corruption.
     */
    private final Object MUTEX = new Object();

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
        this.lastRollover = getTimeAsSecondsSinceEpoch();
    }

    /**
     * Compute the next sequence identifier.
     */
    public SequenceReference next () throws SequenceGeneratorRuntimeException {

        try {

            GlobalMutex globalMutex = globalMutexProvider.get();

            long writerId = globalMutex.getValue();

            if ( writerId < MIN_WRITER_ID ) {
                throw new SequenceGeneratorRuntimeException.WriterIdTooSmall("Writer ID too small: " + writerId);
            }

            if ( writerId > MAX_WRITER_ID ) {
                throw new SequenceGeneratorRuntimeException.WriterIdTooLarge("Writer ID too large: " + writerId);
            }

            long result;

            synchronized( MUTEX ) {

                ++sequence;

                while ( true ) {

                    long now = getTimeAsSecondsSinceEpoch();

                    if ( now > lastRollover ) {
                        sequence     = 0;
                        lastRollover = now;
                    }

                    if ( sequence < MAX_SEQUENCE ) {
                        break;
                    } else {

                        log.warn( "Sequence generator sleeping for %,d for sequence = %s, generator id = %s, last rollover = %s, issued=%s",
                                  SLEEP_TIME, sequence, writerId, lastRollover, issued );

                        // TODO: we could sleep EXACTLY the amount of time needed
                        // until the next rollover but in practice this doesn't
                        // happen very often..

                        sleepUninterruptibly( SLEEP_TIME, TimeUnit.MILLISECONDS );

                    }

                }

                result = ( lastRollover  * GLOBAL_TIME_PADDING) + (writerId * LOCAL_WRITER_ID_PADDING) + sequence;
                ++issued;

            }

            return new SequenceReference( result );

        } catch (GlobalMutexExpiredException e) {
            throw new SequenceGeneratorRuntimeException.GlobalMutexExpired( e );
        }

    }

    protected long getTimeAsSecondsSinceEpoch() {
        return clock.currentTimeMillis() / SequenceReference.TIME_RESOLUTION;
    }

}


