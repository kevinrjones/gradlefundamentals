/*
 * Created on 13-Aug-2004
 *
 */
package com.pluralsight.security;

import com.pluralsight.security.SecurityBase;
import junit.framework.TestCase;

/**
 * @author kevinj
 *
 */
public class TestParseArgs extends TestCase
{
    public void setUp()
    {
        
    }
    
    public void testAllCorrectParamsInOrder()
    {
        SecurityBase h = new SecurityBase();
        
        String args[] = {"-f", "filename", "-d", "destfilename", "-a", "algorithm", "-p", "provider", "-o", "-encode"};
        
        h.parseArgs(args);
        
        assertEquals("filename", h.getFileName());
        assertEquals("destfilename", h.getDestFileName());
        assertEquals("algorithm", h.getAlgorithm());
        assertEquals("provider", h.getProvider());
        assertEquals(true, h.isOverwrite());
        assertEquals(true, h.isEncode());
    }
    
    public void testAllCorrectParamsRandomOrder()
    {
        SecurityBase h = new SecurityBase();
        
        String args[] = {"-p", "provider", "-o", "-d", "destfilename", "-encode", "-f", "filename", "-a", "algorithm", };
        
        h.parseArgs(args);
        
        assertEquals("filename", h.getFileName());
        assertEquals("destfilename", h.getDestFileName());
        assertEquals("algorithm", h.getAlgorithm());
        assertEquals("provider", h.getProvider());
        assertEquals(true, h.isOverwrite());
        assertEquals(true, h.isEncode());
    }
    
    public void testDefaultValues()
    {
        SecurityBase h = new SecurityBase();
        
        String args[] = {};
        
        h.parseArgs(args);
        
        assertEquals(null, h.getFileName());
        assertEquals(null, h.getDestFileName());
        assertEquals(null, h.getProvider());
        assertEquals(false, h.isOverwrite());
        assertEquals(false, h.isEncode());
    }
    
    public void tearDown()
    {        
    }
}
