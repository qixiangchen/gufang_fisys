package com.gf.statusflow;

import org.apache.commons.logging.Log;

public final class Clock
    extends Thread
{

    /**
     * The number of clock ticks in each unsynchronized cycle.
     * The default is 100 milliseconds.
     */
    public static final int UNSYNCH_TICKS = 100;


    /**
     * The number of unsychronized cycles before the clock is
     * synchronized with the system clock. The default is 10.
     */
    public static final int SYNCH_EVERY   = 10;


    /**
     * The current clock.
     */
    private static long   _clock;


    /**
     * The number of clock ticks to skip before incrementing the internal clock.
     */
    private static int    _unsynchTicks;


    /**
     * The number of cycles to skip before synchronizing with the system clock.
     */
    private static int    _synchEvery;


    /**
     * The amount of time in milliseconds by which to advance the clock compared
     * to the system clock.
     */
    private static long   _advance;


    /**
     * Used to adjust the clock when it gets out of synch. Based on the different
     * between the last clock and the system clock at the point of synchronization,
     * divided by synchEvery.
     */
    private static int    _adjust;

      /**
     * Property that determines the number of clock ticks for each
     * unsynchronized cycle. The value is an integer, the percision is
     * milliseconds. The name of this property is <tt>tyrex.clock.unsynchicks</tt>.
     */
    public static final String PROPERTY_UNSYNCH_TICKS = "UUID.clock.unsynchTicks";

    /**
     * Property that determines the number of unsynchronized cycles
     * before the clock is synchronized. The value is an integer.
     * The name of this property is <tt>tyrex.clock.synchEvery</tt>.
     */
    public static final String PROPERTY_SYNCH_EVERY = "UUID.clock.synchEvery";

    /**
     * Returns the current clock.
     *
     * @return The current clock
     */
    public static synchronized long clock()
    {
        // Synchronization is required since clock is a long.
        return _clock;
    }


    /**
     * Sets the number of clock ticks in each unsynchronized cycle.
     * Use zero to restore the default value.
     * <p>
     * The internal clock is advanced every cycle, the length of the
     * cycle is controlled by this property. A higher value results
     * in a lower clock resolution.
     *
     * @param ticks The number of clock ticks (milliseconds) for
     * each unsynchronized cycle
     */
    public static void setUnsynchTicks( int ticks )
    {
        if ( ticks <= 0 )
            ticks = UNSYNCH_TICKS;
        else if ( ticks < 100 )
            ticks = 100;
        _unsynchTicks = ticks;
    }


    /**
     * Returns the number of clock ticks in each unsynchronized cycle.
     *
     * @return The number of clock ticks (milliseconds) for
     * each unsynchronized cycle
     */
    public static int getUnsynchTicks()
    {
        return _unsynchTicks;
    }


    /**
     * Sets the number of unsynchronized cycles before the clock
     * is synchronized with the system clock.
     * <p>
     * Synchronization will occur every <tt>unsynchTicks * synchEvery</tt>
     * milliseconds. The larger the value, the less accurate
     * the clock is.
     *
     * @param every The number of unsynchronized cycles
     */
    public static void setSynchEvery( int every )
    {
        if ( every <= 0 )
            every = SYNCH_EVERY;
        _synchEvery = every;
    }


    /**
     * Artficially advances the clock.
     *
     * @param byMillis The number of milliseconds by which to
     * advance the clock (must be positive)
     */
    public synchronized static void advance( long byMillis )
    {
        // Synchronization is required since clock is a long.
        _advance += byMillis;
        _clock += byMillis;
    }


    /**
     * Returns the number of milliseconds by which the clock is
     * advanced.
     *
     * @return The number of milliseconds by which the clock is
     * advanced
     */
    public static long getAdvance()
    {
        return _advance;
    }


    public void run()
    {
        while ( true ) {
            try {
                for ( int i = 0 ; i < _synchEvery ; ++i ) {
                    sleep( _unsynchTicks );
                    synchronized ( Clock.class ) {
                        _clock += _unsynchTicks + _adjust;
                    }
                }
                synchronize();
            } catch ( Throwable thrw ) {

            }
        }
    }


    public static synchronized long synchronize()
    {
        long current;
        long retarded;
        long clock;
        int  adjust;

        current = System.currentTimeMillis();
        clock = _clock;
        retarded = clock - _advance;
        // Adjust clock to new difference
        if ( current != retarded ) {
            adjust = (int) ( current - retarded ) / _synchEvery;
            if ( adjust != 0 ) {
                _adjust += adjust;
                /*
                if ( Configuration.verbose )
                    Logger.tyrex.debug( "Clock late by " + ( current - retarded ) +
                                        "ms -> synchronized, adjusting by " + _clock._adjust );
                */
            }
        }
        // Make sure clock is progressive
        if ( current > retarded ) {
            clock = current + _advance;
            _clock = clock;
        }
        return clock;
    }


    private Clock()
    {
        super("com.eway.util.clockDaemon" );
        _clock = System.currentTimeMillis();
        setPriority( Thread.MAX_PRIORITY );
        setDaemon( true );
        start();
    }


    static {
        int value;

        value = 100;
        _unsynchTicks = value > 0 ? value : UNSYNCH_TICKS;
        value = 10;
        _synchEvery = value > 0 ? value : SYNCH_EVERY;

        new Clock();
    }


    public static void main( String[] args )
    {
        long clock;
        int  count;

        try {
            count = 1000000;
            System.out.println( "Using Clock.clock()" );
            clock = System.currentTimeMillis();
            for ( int i = 0 ; i < count ; ++i ) {
                if ( ( i % 100 ) == 0 )
                    synchronize();
                else
                    clock();
            }
            clock = System.currentTimeMillis() - clock;
            System.out.println( "Performed " + count + " in " + clock + "ms" );
            System.out.println( "Using System.currentTimeMillis()" );
            clock = System.currentTimeMillis();
            for ( int i = 0 ; i < count ; ++i )
                System.currentTimeMillis();
            clock = System.currentTimeMillis() - clock;
            System.out.println( "Performed " + count + " in " + clock + "ms" );
        } catch ( Exception except ) { }
    }


}

