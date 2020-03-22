/*
 * Created on 16-Aug-2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pluralsight.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author kevinj
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestProperties
{
    @Test
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
