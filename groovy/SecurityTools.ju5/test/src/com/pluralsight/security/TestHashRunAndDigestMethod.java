/*
 * Created on 16-Aug-2004
 *
 */
package com.pluralsight.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import junit.framework.TestCase;

/**
 * @author kevinj
 *
 */
public class TestHashRunAndDigestMethod extends TestCase
{

    private static final String RUNFILEIN = "src/com/pluralsight/security/Hash.java";
    private static final String RUNFILEOUT = "runFile";
    File runFileIn;
    File runFileOut;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        runFileIn = new File(RUNFILEIN);
        runFileOut = new File(RUNFILEOUT);
    }

    public void testRun() throws NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        Hash h = new Hash();
        h.setFileName(RUNFILEIN);
        h.setDestFileName(RUNFILEOUT);
        h.run();
        assertTrue(runFileOut.exists());
    }
    
    public void testDigestArrayGreaterThanBaseSize() throws NoSuchAlgorithmException, IOException
    {
        Hash h = new Hash();
        MessageDigest md = MessageDigest.getInstance("MD5");
                
        byte[] bytesIn = createBytesToHash(250);
        md.update(bytesIn);
        byte[] digestData = md.digest();
        
        
        ByteArrayInputStream bais = createInputStream(bytesIn);
        byte[] hashedBytes = h.digestData(md, bais);
        
        for (int i = 0; i < digestData.length; i++)
        {
            assertEquals(digestData[i], hashedBytes[i]);
        }
    }

    public void testDigestArrayLessThanBaseSize() throws NoSuchAlgorithmException, IOException
    {
        Hash h = new Hash();
        MessageDigest md = MessageDigest.getInstance("MD5");
                
        byte[] bytesIn = createBytesToHash(50);
        md.update(bytesIn);
        byte[] digestData = md.digest();
        
        
        ByteArrayInputStream bais = createInputStream(bytesIn);
        byte[] hashedBytes = h.digestData(md, bais);
        
        for (int i = 0; i < digestData.length; i++)
        {
            assertEquals(digestData[i], hashedBytes[i]);
        }
    }

    public void testDigestArrayEqualsBaseSize() throws NoSuchAlgorithmException, IOException
    {
        Hash h = new Hash();
        MessageDigest md = MessageDigest.getInstance("MD5");
                
        byte[] bytesIn = createBytesToHash(99);
        md.update(bytesIn);
        byte[] digestBytes = md.digest();
                
        ByteArrayInputStream bais = createInputStream(bytesIn);

        byte[] hashedBytes = h.digestData(md, bais);
        
        for (int i = 0; i < digestBytes.length; i++)
        {
            assertEquals(digestBytes[i], hashedBytes[i]);
        }
    }

    private ByteArrayInputStream createInputStream(byte[] bytes)
    {
        return new ByteArrayInputStream(bytes);
    }
    
    /**
     * @return
     */
    private byte[] createBytesToHash(int size)
    {
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++)
        {
            bytes[i] = (byte)i;
        }
        return bytes;
    }

    private ByteArrayOutputStream createOutputStream()
    {
        return new ByteArrayOutputStream();
    }
    
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        runFileOut.delete();
    }

}
