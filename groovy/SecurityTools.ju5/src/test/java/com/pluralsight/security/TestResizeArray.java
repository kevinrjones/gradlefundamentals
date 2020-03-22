/*
 * Created on 13-Aug-2004
 *
 */
package com.pluralsight.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author kevinj
 *
 */
public class TestResizeArray
{

    TestResizeArray()
    {

    }

    @Test
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

    @AfterEach
    protected void tearDown()
    {

    }

}
