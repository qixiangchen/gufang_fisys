package com.gf.statusflow;

import java.util.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.security.SecureRandom;

public final class UUID
{

	public static final String PREFIX = "ID:";


    /**
     * The identifier resolution in bytes. Identifiers are 16-byte
     * long, or 128 bits. Without a prefix, an identifier can be
     * represented as 36 hexadecimal digits and hyphens.
     * (4 hyphens are used in the UUID format)
     */
    public static final int RESOLUTION_BYTES = 16;

    public static final int MAXIMUM_LENGTH = 64;


    /**
     * The maximum length of an identifier prefix. Identifiers
     * created using {@link #create(String) create(String)} with
     * a prefix that is no longer than the maximum prefix size
     * are guaranteed to be within the maximum length allowed
     * and need not be trimmed.
     */
    public static final int MAXIMUM_PREFIX = 28;


    /**
     * UUID state file property that determined the node identifier.
     * The value of this property is an hexadecimal 47-bit value.
     * The name of this property is <tt>uuid.nodeIdentifier</tt>.
     */
    public static final String PROPERTY_NODE_IDENTIFIER = "uuid.nodeIdentifier";


    /**
     * UUID state file property that determined the clock sequence.
     * The value of this property is an hexadecimal 12-bit value.
     * The name of this property is <tt>uuid.clockSequence</tt>.
     */
    public static final String PROPERTY_CLOCK_SEQUENCE = "uuid.clockSequence";


    /**
     * Name of the UUID state file. If no file was specified in the
     * configuration properties, this file name is used. The file
     * name is <tt>uuid.state</tt>.
     */
    public static final String UUID_STATE_FILE = "uuid.state";


    /**
     * The variant value determines the layout of the UUID. This
     * variant is specified in the IETF February 4, 1998 draft.
     * Used for character octets.
     */
    private static final int UUID_VARIANT_OCTET = 0x08;


    /**
     * The variant value determines the layout of the UUID. This
     * variant is specified in the IETF February 4, 1998 draft.
     * Used for byte array.
     */
    private static final int UUID_VARIANT_BYTE = 0x80;


    /**
     * The version identifier for a time-based UUID. This version
     * is specified in the IETF February 4, 1998 draft. Used for
     * character octets.
     */
    private static final int UUID_VERSION_CLOCK_OCTET = 0x01;


    /**
     * The version identifier for a time-based UUID. This version
     * is specified in the IETF February 4, 1998 draft. Used for
     * byte array.
     */
    private static final int UUID_VERSION_CLOCK_BYTE = 0x10;


    /**
     * The version identifier for a name-based UUID. This version
     * is specified in the IETF February 4, 1998 draft. Used for
     * character octets.
     */
    private static final int UUID_VERSION_NAME_OCTET = 0x03;


    /**
     * The version identifier for a name-based UUID. This version
     * is specified in the IETF February 4, 1998 draft. Used for
     * byte array.
     */
    private static final int UUID_VERSION_NAME_BYTE = 0x30;


    /**
     * The version identifier for a random-based UUID. This version
     * is specified in the IETF February 4, 1998 draft. Used for
     * character octets.
     */
    private static final int UUID_VERSION_RANDOM_CLOCK = 0x04;


    /**
     * The version identifier for a random-based UUID. This version
     * is specified in the IETF February 4, 1998 draft. Used for
     * byte array.
     */
    private static final int UUID_VERSION_RANDOM_BYTE = 0x40;


    /**
     * The difference between Java clock and UUID clock. Java clock
     * is base time is January 1, 1970. UUID clock base time is
     * October 15, 1582.
     */
    private static final long JAVA_UUID_CLOCK_DIFF = 0x0b1d069b5400L;


    /**
     * Efficient mapping from 4 bit value to lower case hexadecimal digit.
     */
    private final static char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
                                                            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


    /**
     * The number of UUIDs that can be generated per clock tick.
     * UUID assumes a clock tick every 100 nanoseconds. The actual
     * clock ticks are measured in milliseconds and based on the
     * sync-every property of the clock. The product of these two
     * values is used to set this variable.
     */
    private static int _uuidsPerTick;


    /**
     * The number of UUIDs generated in this clock tick. This counter
     * is reset each time the clock is advanced a tick. When it reaches
     * the maximum number of UUIDs allowed per tick, we block until the
     * clock advances.
     */
    private static int _uuidsThisTick;


    /**
     * The last clock. Whenever the clock changes, we record the last clock
     * to identify when we get a new clock, or when we should increments
     * the UUIDs per tick counter.
     */
    private static long  _lastClock;


    /**
     * The clock sequence. The clock sequence is obtained from the UUID
     * properties and incremented by one each time we boot, or is
     * generated randomaly if missing in the UUID properties. The clock
     * sequence is made of four hexadecimal digits.
     */
    private static char[] _clockSeqOctet;


    /**
     * The clock sequence. The clock sequence is obtained from the UUID
     * properties and incremented by one each time we boot, or is
     * generated randomaly if missing in the UUID properties. The clock
     * sequence is made of two bytes.
     */
    private static byte[] _clockSeqByte;


    /**
     * The node identifier. The node identifier is obtained from the
     * UUID properties, or is generated if missing in the UUID properties.
     * The node identifier is made of twelve hexadecimal digits.
     */
    private static char[] _nodeIdentifierOctet;


    /**
     * The node identifier. The node identifier is obtained from the
     * UUID properties, or is generated if missing in the UUID properties.
     * The node identifier is made of six bytes.
     */
    private static byte[] _nodeIdentifierByte;

    /**
     * Property that determines whether to use secure or standard random
     * number generator. This value is true or false. The name of this
     * property is <tt>tyrex.random.secure</tt>.
     */
    public static final String PROPERTY_SECURE_RANDOM = "UUID.random.secure";


    /**
     * Creates and returns a new identifier.
     *
     * @return An identifier
     */
    public static String create()
    {
        return String.valueOf( createTimeUUIDChars() );
    }


    public static String create( String prefix )
    {
        StringBuffer buffer=new StringBuffer(16);

        if ( prefix == null )
        {
            buffer.append("0000");
        }
        else
        {
			prefix = prefix + "0000";
			buffer.append(prefix.substring(0,4));
        }
        buffer.append( createTimeUUIDChars() );
        return buffer.toString();
    }


    /**
     * Creates and returns a new identifier.
     *
     * @return An identifier
     */
    public static byte[] createBinary()
    {
        return createTimeUUIDBytes();
    }


    /**
     * Converts a prefixed identifier into a byte array. An exception
     * is thrown if the identifier does not match the excepted textual
     * encoding.
     * <p>
     * The format for the identifier is <code>prefix{nn|-}*</code>:
     * a prefix followed by a sequence of bytes, optionally separated
     * by hyphens. Each byte is encoded as a pair of hexadecimal digits.
     *
     * @param prefix The identifier prefix
     * @param identifier The prefixed identifier
     * @return The identifier as an array of bytes
     * @throws InvalidIDException The identifier does not begin with
     * the prefix, or does not consist of a sequence of hexadecimal
     * digit pairs, optionally separated by hyphens
     */
    public static byte[] toBytes( String prefix, String identifier )
        throws InvalidIDException
    {
        int    index;
        char   digit;
        byte   nibble;
        byte[] bytes;
        byte[] newBytes;

        if ( identifier == null )
            throw new IllegalArgumentException( "Argument identifier is null" );
        if ( prefix == null )
            throw new IllegalArgumentException( "Argument prefix is null" );
        if ( ! identifier.startsWith( prefix ) )
            throw new InvalidIDException("");

        index = 0;
        bytes = new byte[ ( identifier.length() - prefix.length() ) / 2 ];
        for ( int i = prefix.length() ; i < identifier.length() ; ++i ) {
            digit = identifier.charAt( i );
            if ( digit == '-' )
                continue;
            if ( digit >= '0' && digit <= '9' )
                nibble = (byte) ( ( digit - '0' ) << 4 );
            else if ( digit >= 'A' && digit <= 'F' )
                nibble = (byte) ( ( digit - ( 'A' - 0x0A ) ) << 4 );
            else if ( digit >= 'a' && digit <= 'f' )
                nibble = (byte) ( ( digit - ( 'a' - 0x0A ) ) << 4 );
            else
                throw new InvalidIDException("");
            ++i;
            if ( i == identifier.length() )
                throw new InvalidIDException("");
            digit = identifier.charAt( i );
            if ( digit >= '0' && digit <= '9' )
                nibble = (byte) ( nibble | ( digit - '0' ) );
            else if ( digit >= 'A' && digit <= 'F' )
                nibble = (byte) ( nibble | ( digit - ( 'A' - 0x0A ) ) );
            else if ( digit >= 'a' && digit <= 'f' )
                nibble = (byte) ( nibble | ( digit - ( 'a' - 0x0A ) ) );
            else
                throw new InvalidIDException("");
            bytes[ index ] = nibble;
            ++index;
        }
        if ( index == bytes.length )
            return bytes;
        newBytes = new byte[ index ];
        while ( index-- > 0 )
            newBytes[ index ] = bytes[ index ];
        return newBytes;
    }


    /**
     * Converts an identifier into a byte array. An exception is
     * thrown if the identifier does not match the excepted textual
     * encoding.
     * <p>
     * The format for the identifier is <code>{nn|-}*</code>:
     * a sequence of bytes, optionally separated by hyphens.
     * Each byte is encoded as a pair of hexadecimal digits.
     *
     * @param identifier The identifier
     * @return The identifier as an array of bytes
     * @throws InvalidIDException The identifier does not consist
     * of a sequence of hexadecimal digit pairs, optionally separated
     * by hyphens
     */
    public static byte[] toBytes( String identifier )
        throws InvalidIDException
    {
        int    index;
        char   digit;
        byte   nibble;
        byte[] bytes;
        byte[] newBytes;

        if ( identifier == null )
            throw new IllegalArgumentException( "Argument identifier is null" );
        index = 0;
        bytes = new byte[ identifier.length() / 2 ];
        for ( int i = 0 ; i < identifier.length() ; ++i ) {
            digit = identifier.charAt( i );
            if ( digit == '-' )
                continue;
            if ( digit >= '0' && digit <= '9' )
                nibble = (byte) ( ( digit - '0' ) << 4 );
            else if ( digit >= 'A' && digit <= 'F' )
                nibble = (byte) ( ( digit - ( 'A' - 0x0A ) ) << 4 );
            else if ( digit >= 'a' && digit <= 'f' )
                nibble = (byte) ( ( digit - ( 'a' - 0x0A ) ) << 4 );
            else
                throw new InvalidIDException( "无效的字符" );
            ++i;
            if ( i == identifier.length() )
                throw new InvalidIDException("无效的数字");
            digit = identifier.charAt( i );
            if ( digit >= '0' && digit <= '9' )
                nibble = (byte) ( nibble | ( digit - '0' ) );
            else if ( digit >= 'A' && digit <= 'F' )
                nibble = (byte) ( nibble | ( digit - ( 'A' - 0x0A ) ) );
            else if ( digit >= 'a' && digit <= 'f' )
                nibble = (byte) ( nibble | ( digit - ( 'a' - 0x0A ) ) );
            else
                throw new InvalidIDException( "无效的字符" );
            bytes[ index ] = nibble;
            ++index;
        }
        if ( index == bytes.length )
            return bytes;
        newBytes = new byte[ index ];
        while ( index-- > 0 )
            newBytes[ index ] = bytes[ index ];
        return newBytes;
    }


    /**
     * Converts a byte array into a prefixed identifier.
     * <p>
     * The format for the identifier is <code>prefix{nn|-}*</code>:
     * a prefix followed by a sequence of bytes, optionally separated
     * by hyphens. Each byte is encoded as a pair of hexadecimal digits.
     *
     * @param prefix The identifier prefix
     * @param byte An array of bytes
     * @return A string representation of the identifier
     */
    public static String fromBytes( String prefix, byte[] bytes )
    {
        StringBuffer buffer;

        if ( prefix == null )
            throw new IllegalArgumentException( "Argument prefix is null" );
        if ( bytes == null || bytes.length == 0 )
            throw new IllegalArgumentException( "Argument bytes is null or an empty array" );
        buffer = new StringBuffer( prefix );
        for ( int i = 0 ; i < bytes.length ; ++i ) {
            buffer.append( HEX_DIGITS[ ( bytes[ i ] & 0xF0 ) >> 4 ] );
            buffer.append( HEX_DIGITS[ ( bytes[ i ] & 0x0F ) ] );
        }
        return buffer.toString();
    }


    /**
     * Converts a byte array into an identifier.
     * <p>
     * The format for the identifier is <code>{nn|-}*</code>: a sequence
     * of bytes, optionally separated by hyphens. Each byte is encoded as
     * a pair of hexadecimal digits.
     *
     * @param byte An array of bytes
     * @return A string representation of the identifier
     */
    public static String fromBytes( byte[] bytes )
    {
        StringBuffer buffer;

        if ( bytes == null || bytes.length == 0 )
            throw new IllegalArgumentException( "Argument bytes is null or an empty array" );
        buffer = new StringBuffer();
        for ( int i = 0 ; i < bytes.length ; ++i ) {
            buffer.append( HEX_DIGITS[ ( bytes[ i ] & 0xF0 ) >> 4 ] );
            buffer.append( HEX_DIGITS[ ( bytes[ i ] & 0x0F ) ] );
        }
        return buffer.toString();
    }


    /**
     * Truncates an identifier so that it does not extend beyond
     * {@link #MAXIMUM_LENGTH} characters in length.
     *
     * @param identifier An identifier
     * @return An identifier trimmed to {@link #MAXIMUM_LENGTH} characters
     */
    public static String trim( String identifier )
    {
        if ( identifier == null )
            throw new IllegalArgumentException( "Argument identifier is null" );
        if ( identifier.length() > MAXIMUM_LENGTH )
            return identifier.substring( 0, MAXIMUM_LENGTH );
        return identifier;
    }


    /**
     * Returns a time-based UUID as a character array. The UUID
     * identifier is always 36 characters long.
     *
     * @return A time-based UUID
     */
    public static char[] createTimeUUIDChars()
    {
        long   clock;
        char[] chars;
        long   nextClock;

        // Acquire lock to assure synchornized generation
        synchronized ( UUID.class ) {
            clock = Clock.clock();
            while ( true ) {
                if ( clock > _lastClock ) {
                    // Since we are using the clock interval for the UUID space,
                    // we must make sure the next clock provides sufficient
                    // room so UUIDs do not roll over.
                    nextClock = _lastClock + ( _uuidsThisTick / 100 );
                    if ( clock <= nextClock )
                        clock = Clock.synchronize();
                    if ( clock > nextClock ) {
                        // Clock reading changed since last UUID generated,
                        // reset count of UUIDs generated with this clock.
                        _uuidsThisTick = 0;
                        _lastClock = clock;
                        // Adjust UUIDs per tick in case the clock sleep ticks
                        // have changed.
                        _uuidsPerTick = Clock.getUnsynchTicks() * 100;
                        break;
                    }
                }

                if ( _uuidsThisTick + 1 < _uuidsPerTick ) {
                    // Clock did not advance, but able to create more UUIDs
                    // for this clock, proceed.
                    ++ _uuidsThisTick;
                    break;
                }

                // Running out of UUIDs for the current clock tick, must
                // wait until clock advances. Possible that clock did not
                // advance in background, so try to synchronize it first.
                clock = Clock.synchronize();
                if ( clock <= _lastClock ) {
                    while ( clock <= _lastClock ) {
                        // UUIDs generated too fast, suspend for a while.
                        try {
                            Thread.currentThread().sleep( Clock.getUnsynchTicks() );
                        } catch ( InterruptedException except ) { }
                        clock = Clock.synchronize();
                    }
                }
            }

            // Modify Java clock (milliseconds) to UUID clock (100 nanoseconds).
            // Add the count of uuids to low order bits of the clock reading,
            // assuring we get a unique clock.
            clock = ( _lastClock + JAVA_UUID_CLOCK_DIFF ) * 100 + _uuidsThisTick;

            chars=new char[12];
            chars[ 0 ]  = HEX_DIGITS[ (int) ( ( clock >> 28 ) & 0x0F ) ];
            chars[ 1 ]  = HEX_DIGITS[ (int) ( ( clock >> 24 ) & 0x0F ) ];
            chars[ 2 ]  = HEX_DIGITS[ (int) ( ( clock >> 20 ) & 0x0F ) ];
            chars[ 3 ]  = HEX_DIGITS[ (int) ( ( clock >> 16 ) & 0x0F ) ];
            chars[ 4 ]  = HEX_DIGITS[ (int) ( ( clock >> 12 ) & 0x0F ) ];
            chars[ 5 ]  = HEX_DIGITS[ (int) ( ( clock >> 8 ) & 0x0F ) ];
            chars[ 6 ]  = HEX_DIGITS[ (int) ( ( clock >> 4 ) & 0x0F ) ];
            chars[ 7 ]  = HEX_DIGITS[ (int) ( clock & 0x0F ) ];
            chars[ 8 ] = _clockSeqOctet[ 0 ];
            chars[ 9 ] = _clockSeqOctet[ 1 ];
            chars[ 10 ] = _clockSeqOctet[ 2 ];
            chars[ 11 ] = _clockSeqOctet[ 3 ];
            /**
            chars = new char[ 36 ];
            // Add the low field of the clock (4 octets )
            chars[ 0 ]  = HEX_DIGITS[ (int) ( ( clock >> 28 ) & 0x0F ) ];
            chars[ 1 ]  = HEX_DIGITS[ (int) ( ( clock >> 24 ) & 0x0F ) ];
            chars[ 2 ]  = HEX_DIGITS[ (int) ( ( clock >> 20 ) & 0x0F ) ];
            chars[ 3 ]  = HEX_DIGITS[ (int) ( ( clock >> 16 ) & 0x0F ) ];
            chars[ 4 ]  = HEX_DIGITS[ (int) ( ( clock >> 12 ) & 0x0F ) ];
            chars[ 5 ]  = HEX_DIGITS[ (int) ( ( clock >> 8 ) & 0x0F ) ];
            chars[ 6 ]  = HEX_DIGITS[ (int) ( ( clock >> 4 ) & 0x0F ) ];
            chars[ 7 ]  = HEX_DIGITS[ (int) ( clock & 0x0F ) ];
            chars[ 8 ]  = '-';
            // Add the medium field of the clock (2 octets)
            chars[ 9 ]  = HEX_DIGITS[ (int) ( ( clock >> 44 ) & 0x0F ) ];
            chars[ 10 ] = HEX_DIGITS[ (int) ( ( clock >> 40 ) & 0x0F ) ];
            chars[ 11 ] = HEX_DIGITS[ (int) ( ( clock >> 36 ) & 0x0F ) ];
            chars[ 12 ] = HEX_DIGITS[ (int) ( ( clock >> 32 ) & 0x0F ) ];
            chars[ 13 ] = '-';
            // Add the high field of the clock multiplexed with version number (2 octets)
            chars[ 14 ] = HEX_DIGITS[ (int) ( ( ( clock >> 60 ) & 0x0F ) | UUID_VERSION_CLOCK_OCTET ) ];
            chars[ 15 ] = HEX_DIGITS[ (int) ( ( clock >> 56 ) & 0x0F ) ];
            chars[ 16 ] = HEX_DIGITS[ (int) ( ( clock >> 52 ) & 0x0F ) ];
            chars[ 17 ] = HEX_DIGITS[ (int) ( ( clock >> 48 ) & 0x0F ) ];
            chars[ 18 ] = '-';
            // Add the clock sequence and version identifier (2 octets)
            chars[ 19 ] = _clockSeqOctet[ 0 ];
            chars[ 20 ] = _clockSeqOctet[ 1 ];
            chars[ 21 ] = _clockSeqOctet[ 2 ];
            chars[ 22 ] = _clockSeqOctet[ 3 ];
            chars[ 23 ] = '-';
            // Add the node identifier (6 octets)
            chars[ 24 ] = _nodeIdentifierOctet[ 0 ];
            chars[ 25 ] = _nodeIdentifierOctet[ 1 ];
            chars[ 26 ] = _nodeIdentifierOctet[ 2 ];
            chars[ 27 ] = _nodeIdentifierOctet[ 3 ];
            chars[ 28 ] = _nodeIdentifierOctet[ 4 ];
            chars[ 29 ] = _nodeIdentifierOctet[ 5 ];
            chars[ 30 ] = _nodeIdentifierOctet[ 6 ];
            chars[ 31 ] = _nodeIdentifierOctet[ 7 ];
            chars[ 32 ] = _nodeIdentifierOctet[ 8 ];
            chars[ 33 ] = _nodeIdentifierOctet[ 9 ];
            chars[ 34 ] = _nodeIdentifierOctet[ 10 ];
            chars[ 35 ] = _nodeIdentifierOctet[ 11 ];
             */
        }
        return chars;
    }


    /**
     * Returns a time-based UUID as a character array. The UUID
     * identifier is always 16 bytes long.
     *
     * @return A time-based UUID
     */
    public static byte[] createTimeUUIDBytes()
    {
        long   clock;
        byte[] bytes;
        long   nextClock;

        // Acquire lock to assure synchornized generation
        synchronized ( UUID.class ) {
            clock = Clock.clock();
            while ( true ) {
                if ( clock > _lastClock ) {
                    // Since we are using the clock interval for the UUID space,
                    // we must make sure the next clock provides sufficient
                    // room so UUIDs do not roll over.
                    nextClock = _lastClock + ( _uuidsThisTick / 100 );
                    if ( clock <= nextClock )
                        clock = Clock.synchronize();
                    if ( clock > nextClock ) {
                        // Clock reading changed since last UUID generated,
                        // reset count of UUIDs generated with this clock.
                        _uuidsThisTick = 0;
                        _lastClock = clock;
                        // Adjust UUIDs per tick in case the clock sleep ticks
                        // have changed.
                        _uuidsPerTick = Clock.getUnsynchTicks() * 100;
                        break;
                    }
                }

                if ( _uuidsThisTick + 1 < _uuidsPerTick ) {
                    // Clock did not advance, but able to create more UUIDs
                    // for this clock, proceed.
                    ++ _uuidsThisTick;
                    break;
                }

                // Running out of UUIDs for the current clock tick, must
                // wait until clock advances. Possible that clock did not
                // advance in background, so try to synchronize it first.
                clock = Clock.synchronize();
                if ( clock <= _lastClock ) {
                    while ( clock <= _lastClock ) {
                        // UUIDs generated too fast, suspend for a while.
                        try {
                            Thread.currentThread().sleep( Clock.getUnsynchTicks() );
                        } catch ( InterruptedException except ) { }
                        clock = Clock.synchronize();
                    }
                }
            }

            // Modify Java clock (milliseconds) to UUID clock (100 nanoseconds).
            // Add the count of uuids to low order bits of the clock reading,
            // assuring we get a unique clock.
            clock = ( _lastClock + JAVA_UUID_CLOCK_DIFF ) * 100 + _uuidsThisTick;

            bytes = new byte[ 16 ];
            // Add the low field of the clock (4 octets )
            bytes[ 0 ]  = (byte) ( ( clock >> 24 ) & 0xFF );
            bytes[ 1 ]  = (byte) ( ( clock >> 16 ) & 0xFF );
            bytes[ 2 ]  = (byte) ( ( clock >> 8 ) & 0xFF );
            bytes[ 3 ]  = (byte) ( clock & 0xFF );
            // Add the medium field of the clock (2 octets)
            bytes[ 4 ]  = (byte) ( ( clock >> 40 ) & 0xFF );
            bytes[ 5 ]  = (byte) ( ( clock >> 32 ) & 0xFF );
            // Add the high field of the clock multiplexed with version number (2 octets)
            bytes[ 6 ]  = (byte) ( ( ( clock >> 60 ) & 0xFF ) | UUID_VERSION_CLOCK_BYTE );
            bytes[ 7 ]  = (byte) ( ( clock >> 48 ) & 0xFF );
            // Add the clock sequence and version identifier (2 octets)
            bytes[ 8 ] = _clockSeqByte[ 0 ];
            bytes[ 9 ] = _clockSeqByte[ 1 ];
            // Add the node identifier (6 octets)
            bytes[ 10 ] = _nodeIdentifierByte[ 0 ];
            bytes[ 11 ] = _nodeIdentifierByte[ 1 ];
            bytes[ 12 ] = _nodeIdentifierByte[ 2 ];
            bytes[ 13 ] = _nodeIdentifierByte[ 3 ];
            bytes[ 14 ] = _nodeIdentifierByte[ 4 ];
            bytes[ 15 ] = _nodeIdentifierByte[ 5 ];
        }
        return bytes;
    }


    /**
     * Returns true if the UUID was created on this machine.
     * Determines the source of the UUID based on the node
     * identifier.
     *
     * @param uuid The UUID as a byte array
     * @return True if created on this machine
     */
    public static boolean isLocal( byte[] uuid )
    {
        if ( uuid == null )
            throw new IllegalArgumentException( "Argument uuid is null" );
        if ( uuid.length != 16 )
            return false;
        return ( uuid[ 10 ] == _nodeIdentifierByte[ 0 ] &&
                 uuid[ 11 ] == _nodeIdentifierByte[ 1 ] &&
                 uuid[ 12 ] == _nodeIdentifierByte[ 2 ] &&
                 uuid[ 13 ] == _nodeIdentifierByte[ 3 ] &&
                 uuid[ 14 ] == _nodeIdentifierByte[ 4 ] &&
                 uuid[ 15 ] == _nodeIdentifierByte[ 5 ] );
    }

    /**
     * Read the UUID state and set the node identifier and clock
     * sequence. This method is called exactly once, the first
     * time a UUID is requested.
     * <p>
     * If attempts to load the UUID state from the UUID state
     * file. If the file is not found, or does not contain a node
     * identifier, a random node identifier is created.
     * <p>
     * This method sets {@link #_uuidsPerTick} to the number of
     * UUIDs allowed per clock tick.
     */
    private static void loadState()
    {
        String           stateFile;
        FileInputStream  input;
        FileOutputStream output;
        Properties       state;
        String           nodeIdString;
        long             nodeIdLong;
        String           seqString;
        int              seqInt;
        StringTokenizer  tokenizer;
        String           token;

        // Find the name of the UUID state file from the configuration,
        // or use a default name.
        stateFile = null;
        if ( stateFile == null )
            stateFile = UUID_STATE_FILE;
        state = null;
        // Try to read the UUID state file into a properties object.
        // If successful, the values will be accessible from the
        // object state, otherwise, state is set to null.
        try {
            input = new FileInputStream( stateFile );
            state = new Properties();
            state.load( input );
            input.close();
        } catch ( IOException except ) {
             state = null;
        }

        // Minus one means the node identifier or clock sequence could not be determined.
        nodeIdLong = -1;
        seqInt = -1;
        // Read the node identifier from the UUID properties. If the node identifier
        // cannot be determined (missing, or invalid), generate a new node identifier
        // and new random clock sequence.
        if ( state != null ) {
            nodeIdString = state.getProperty( PROPERTY_NODE_IDENTIFIER );
            if ( nodeIdString != null ) {
                try {
                    nodeIdString = nodeIdString.trim();
                    nodeIdLong = 0;
                    tokenizer = new StringTokenizer( nodeIdString, ":" );
                    while ( tokenizer.hasMoreTokens() ) {
                        token = tokenizer.nextToken();
                        nodeIdLong = ( nodeIdLong << 8 ) + Long.parseLong( token, 16 );
                    }
                    // This is the only case where we can read the clock sequence. If the
                    // clock sequence cannot be determined (missing, or invalid), the node
                    // identifier is considered invalid.
                    seqString = state.getProperty( PROPERTY_CLOCK_SEQUENCE );
                    if ( seqString != null ) {
                        try {
                            seqString = seqString.trim();
                            seqInt = Integer.parseInt( seqString, 10 );
                            ++seqInt;
                        } catch ( NumberFormatException except ) {
                            nodeIdLong = -1;
                        }
                    }
                } catch ( NumberFormatException except ) {
                }
            }
        }

        // Convert clock sequence to 4 hexadecimal digits so it can be stored
        // in the UUID state file.
        if ( seqInt == - 1 )
            seqInt = getRandom().nextInt( 1 << 12 );
        seqInt = seqInt & 0x1FFF;
        _clockSeqOctet = new char[ 4 ];
        _clockSeqOctet[ 0 ] = HEX_DIGITS[ (int) ( ( seqInt >> 12 ) & 0x0F ) ];
        _clockSeqOctet[ 1 ] = HEX_DIGITS[ (int) ( ( seqInt >> 8 ) & 0x0F ) ];
        _clockSeqOctet[ 2 ] = HEX_DIGITS[ (int) ( ( seqInt >> 4 ) & 0x0F ) ];
        _clockSeqOctet[ 3 ] = HEX_DIGITS[ (int) ( seqInt & 0x0F ) ];
        _clockSeqByte = new byte[ 2 ];
        _clockSeqByte[ 0 ] = (byte) ( ( seqInt >> 8 ) & 0xFF );
        _clockSeqByte[ 1 ] = (byte) ( seqInt & 0xFF );

        // If the state file exists, then we read the node identifier from a file,
        // and are expected to store the clock sequence in that file. If we fail to
        // store the new clock sequence, must assume a random node identifier.
        if ( nodeIdLong != -1 && state != null ) {
            state.put( PROPERTY_CLOCK_SEQUENCE, String.valueOf( seqInt ) );
            try {
                output = new FileOutputStream( stateFile  );
                state.save( output, "uuid.stateFileHeader" ) ;
                output.close();
            } catch ( IOException except ) {
                nodeIdLong = -1;
            }
        }

        // If the node identifier could not be determined, or the state file could
        // not be updated, obtain the node identifier and clock sequence from
        // a random number.
        if ( nodeIdLong == -1 ) {
            // If node identifier is not IEEE 802 address, it must have the bit 48 set.
            nodeIdLong = getRandom().nextLong();
            nodeIdLong = nodeIdLong | ( 1 << 47 );
            seqInt = getRandom().nextInt( 1 << 12 );
            seqInt = seqInt & 0x1FFF;
            _clockSeqOctet = new char[ 4 ];
            _clockSeqOctet[ 1 ] = HEX_DIGITS[ (int) ( ( seqInt >> 8 ) & 0x0F ) ];
            _clockSeqOctet[ 2 ] = HEX_DIGITS[ (int) ( ( seqInt >> 4 ) & 0x0F ) ];
            _clockSeqOctet[ 3 ] = HEX_DIGITS[ (int) ( seqInt & 0x0F ) ];
            _clockSeqByte = new byte[ 2 ];
            _clockSeqByte[ 0 ] = (byte) ( ( seqInt >> 8 ) & 0xFF );
            _clockSeqByte[ 1 ] = (byte) ( seqInt & 0xFF );
        }

        // Convert node identifier to 12 hexadecimal digits, and sequence number
        // to 4 hexadecimal digits.
        _nodeIdentifierOctet = new char[ 12 ];
        for ( int i = 0 ; i < 12 ; ++i )
            _nodeIdentifierOctet[ i ] = HEX_DIGITS[ (int) ( ( nodeIdLong >> ( ( 11 - i ) * 4 ) ) & 0x0F ) ];
        _nodeIdentifierByte = new byte[ 6 ];
        for ( int i = 0 ; i < 6 ; ++i )
            _nodeIdentifierByte[ i ] = (byte) ( ( nodeIdLong >> ( ( 5 - i ) * 8 ) ) & 0xFF );
        nodeIdString = new String();
        for ( int i = 0 ; i < 12 ; i += 2 ) {
            if ( i > 0 )
                nodeIdString = nodeIdString + ":";
            nodeIdString = nodeIdString + HEX_DIGITS[ (int) ( ( nodeIdLong >> ( ( 11 - i ) * 4 ) ) & 0x0F ) ] +
                HEX_DIGITS[ (int) ( ( nodeIdLong >> ( ( 10 - i ) * 4 ) ) & 0x0F ) ];
        }

        // The number of UUIDs allowed per tick depends on the number of ticks between
        // each advance of the clock, adjusted for 100 nanosecond precision.
        _uuidsPerTick = Clock.getUnsynchTicks() * 100;

        // Need to mask UUID variant on clock sequence, but only after clock sequence
        // has been stored.
        _clockSeqOctet[ 0 ] = HEX_DIGITS[ (int) ( ( seqInt >> 12 ) & 0x0F ) | UUID_VARIANT_OCTET ];
        _clockSeqByte[ 0 ] = (byte) ( ( ( seqInt >> 8 ) & 0xFF ) | UUID_VARIANT_BYTE );
    }


    static {
        loadState();
        // This makes sure we miss at least one clock tick, just to be safe.
        _uuidsThisTick = _uuidsPerTick;
        _lastClock = Clock.clock();
    }


    public static void main( String[] args ) {
        long     clock;
        HashSet  hash;
        String   id;
        int      count = 1000000;

        for ( int i = 0 ; i < 10 ; ++i ) {
            System.out.println( create() );
        }
        clock = System.currentTimeMillis();
        hash = new HashSet( count / 100, 100 );
        for ( int i = 0 ; i < count ; ++i ) {
            if ( ( i % 10000 ) == 0 )
                System.out.println( "Checked " + i );
            id = create();
            if ( hash.contains( id ) )
                System.out.println( "Duplicate id " + id );
            else
                hash.add( id );
        }
        clock =  System.currentTimeMillis() - clock;
        System.out.println( "Generated " + count + " UUIDs in " + clock + "ms" );
    }


    /**
     * An exception indicating the identifier is invalid and
     * cannot be converted into an array of bytes.
     */
    public static class InvalidIDException
        extends Exception
    {


        public InvalidIDException( String message )
        {
            super( message );
        }


    }

    /**
       * Returns a random number generator. Depending on the configuration this is
       * either a secure random number generator, or a standard random number generator
       * seeded with the system clock.
       *
       * @return A random number generator
       */
      public static synchronized Random getRandom()
      {
          if ( _random == null ) {
              if ( false ) {
                  _random = new SecureRandom();
              } else {
                  _random = new Random( System.currentTimeMillis() + Runtime.getRuntime().freeMemory() );
              }
          }
          return _random;
      }

        /**
     * The random number generator. This variable is set on-demand.
     */
    private static Random        _random;

}

