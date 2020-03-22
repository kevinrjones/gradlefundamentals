/*
 * Created on 13-Aug-2004
 *
 */
package com.pluralsight.security;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author kevinj
 *
 */
public class TestResizeArray extends TestCase
{

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /**
     * @throws IOException
     * resizeBytesArray doubles the size of the bytesToHash array whenever it is
     * full of data.
     *
     */    
    public void testArrayResize() throws IOException
    {
        byte[] in = new byte[101];
        for (int i = 0; i < in.length; i++)
        {
            in[i] = (byte)i;
        }
        SecurityBase h = new SecurityBase();
        byte[] result = h.resizeArray(in);
        
        assertEquals(202, result.length);
        assertEquals(in[99], result[99]);
        assertEquals(in[100], result[100]);
        assertEquals(in[0], result[0]);
    }
    
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
