/*
 * Created on 16-Aug-2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pluralsight.security;

import com.pluralsight.security.Hash;
import junit.framework.TestCase;

/**
 * @author kevinj
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestProperties extends TestCase
{

    public void testBytesToHash()
    {
        Hash h = new Hash();
        byte[] bytes = new byte[50];
        
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte)i;
        }
        
        h.setBytesToHash(bytes);
        for (int i = 0; i < bytes.length; i++)
        {
            assertEquals(bytes[i], h.getBytesToHash()[i]);
        }
    }
}
