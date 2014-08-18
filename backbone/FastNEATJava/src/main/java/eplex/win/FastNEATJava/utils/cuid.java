package eplex.win.FastNEATJava.utils;

import android.provider.Settings;

import java.util.Date;

/**
 * cuid.js
 * Collision-resistant UID generator for browsers and node.
 * Sequential for fast db lookups and recency sorting.
 * Safe for element IDs and server-side lookups.
 *
 * Extracted from CLCTR
 * 
 * Copyright (c) Eric Elliott 2012
 * MIT License
 */
//From: https://github.com/dilvie/cuid

//note that module.exports is at the end -- it exports the api variable

/*global window, navigator, document, require, process, module */



//singleton for UUID generation!
//threadsafe and keeps a counter to prevent local (unlikely) collisions
public class cuid
{
    static int c =0;
    static int blockSize =4;
    static int base = 36;
    static int discreteValues = (int)Math.pow(base, blockSize);

    static String pad(String num, int size)
    {
        String s = "000000000" + num;
        return s.substring(s.length() - size);
    }

    static String randomBlock() {
        return pad(Integer.toString((int) (Math.random() * (discreteValues << 0)), base), blockSize);
    }

    private static cuid instance = null;
    protected cuid() {
        // Exists only to defeat instantiation.
    }
    public static cuid getInstance() {
        if(instance == null) {
            instance = new cuid();
        }
        return instance;
    }

    //send back the process ID padded out to 4 digits!
    static String fingerprint()
    {
        return pad(Integer.toString(android.os.Process.myPid(), base), blockSize);
    }

    //we want predictable string with similar properties across all machines
    //that is, we don't want uniqueness here
    public String generate(int ix)
    {
        // Starting with a lowercase letter makes
        // it HTML element ID friendly.
        String letter = "c"; // hard-coded allows for sequential access

        // timestamp variant -- the number!
        // warning: this exposes the exact order
        // that the uid was created.
        String timestamp = Long.toString(ix, base);

        // Encourage same-machine collisions! Always 0 counter - but importantly keeps the same block size
        String counter = pad(Integer.toString(0, base), blockSize);

        // Encourage same-machine collisions! Always 0 fingerprint - but importantly keeps the same block size
        String fingerprint = pad(Integer.toString(0, base), blockSize);

        // No randomness in this block
        String random =  pad(Integer.toString(0, base), 4*blockSize);

        return  (letter + timestamp + counter + fingerprint + random);
    }

    public String generate()
    {
        // Starting with a lowercase letter makes
        // it HTML element ID friendly.
        String letter = "c"; // hard-coded allows for sequential access

        // timestamp
        // warning: this exposes the exact date and time
        // that the uid was created.
        String timestamp = Long.toString(new Date().getTime(), base);

        // Prevent same-machine collisions.
        String counter;

        // A few chars to generate distinct ids for different
        // clients (so different computers are far less
        // likely to generate the same id)
        String fingerprint = fingerprint();

        // Grab some more chars from Math.random()
        String random = randomBlock() + randomBlock() + randomBlock() + randomBlock();

        synchronized (this) {
            c = (c < discreteValues) ? c : 0;

            counter = pad(Integer.toString(c, base), blockSize);

            c++; // this is not subliminal
        }

        return  (letter + timestamp + counter + fingerprint + random);
    }

    public static boolean isLessThan(String first, String second)
    {
        //tease apart first, second to determine which ID came first
        //counter + fingerprint + random = 6 blocks of 4 = 24
        int dateEnd = 6*blockSize;
        int counterEnd = 5*blockSize;
        int charStart = 1;

        //convert the base-36 time string to base 10 number -- parseint handles this by sending in the original radix
        int firstTime = Integer.parseInt(first.substring(charStart, first.length() - dateEnd), base);
        //ditto for counter
        int firstCounter = Integer.parseInt(first.substring(first.length() - dateEnd, first.length() - counterEnd), base);

        //convert the base-36 time string to base 10 number -- parseint handles this by sending in the original radix
        int secondTime =  Integer.parseInt(second.substring(charStart, second.length() - dateEnd), base);

        //ditto for counter
        int secondCounter = Integer.parseInt(second.substring(second.length() - dateEnd, second.length() - counterEnd), base);

        //either the first time is less than the second time, and we answer this question immediately
        //or the times are equal -- then we pull the lower counter
        //techincially counters can wrap, but this won't happen very often AND this is all for measuring disjoint/excess behavior
        //the time should be enough of an ordering principal for this not to matter
        return firstTime < secondTime || (firstTime == secondTime && firstCounter < secondCounter);
    }

}


