package com.pluralsight.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author kevinj
 *
 */
public class TestReadWriteBytes
{
    @Test
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

    @Test
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

    @Test
    public void testWriteBytesEncoded() throws IOException
    {
        SecurityBase h = new SecurityBase();
        
        byte[] bytes = new byte[200];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte)i;
        }
        
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
