package com.pluralsight.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

/**
 * @author kevinj
 *
 */
public class TestReadWriteBytes extends TestCase
{
    public void testReadBytes() throws IOException
    {
        SecurityBase h = new SecurityBase();
        
        byte[] bytes = new byte[200];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte)i;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        
        
        byte[] bytesToHash = h.readBytes(bais);
        
        assertEquals(bytes.length, bytesToHash.length);
        for (int i = 0; i < bytes.length; i++)
        {
            assertEquals(bytes[i], bytesToHash[i]);
        }
    }

    public void testWriteBytes() throws IOException
    {
        SecurityBase h = new SecurityBase();
        
        byte[] bytes = new byte[200];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte)i;
        }
                
        ByteArrayOutputStream bais = new ByteArrayOutputStream();        
        
        h.writeBytes(bais, bytes);
                        
        byte[] writtenBytes = bais.toByteArray();
        for (int i = 0; i < bytes.length; i++)
        {
            assertEquals(bytes[i], writtenBytes[i]);
        }
    }

    public void testWriteBytesEncoded() throws IOException
    {
        SecurityBase h = new SecurityBase();
        
        byte[] bytes = new byte[200];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte)i;
        }
        
       // BASE64Encoder encoder = new BASE64Encoder();

        String encoded = printBase64Binary(bytes);

        ByteArrayOutputStream bais = new ByteArrayOutputStream();        
        
        h.setEncode(true);
        h.writeBytes(bais, bytes);
                        
        byte[] writtenBytes = bais.toByteArray();
        byte[] originalBytesEncoded = encoded.getBytes();
        for (int i = 0; i < originalBytesEncoded.length; i++)
        {
            assertEquals(originalBytesEncoded[i], writtenBytes[i]);
        }
    }

}
