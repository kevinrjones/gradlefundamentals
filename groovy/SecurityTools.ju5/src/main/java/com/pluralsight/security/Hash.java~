/*
 * Created on 16-Aug-2004
 *
 */
package com.pluralsight.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author kevinj
 *  
 */
@SuppressWarnings("deprecation")
public class Hash extends SecurityBase
{    
    static Logger logger = Logger.getLogger(Hash.class);

    byte[] bytesToHash;
    static final int ARRAY_INITIAL_SIZE = 100;

    public static void main(String[] args)
    {

        URL url = Hash.class.getResource("/log4j.properties");
        
        if(url != null)
        {
            PropertyConfigurator.configure(url);
        }


        if(args.length == 0 || (args.length == 1 && args[0].equals("--help")))
        {
            System.err.println("usage: java Hash [-f filename]  [-d destfilename] [-p provider] [-a algorithm] [-o] [-encode]");
            System.err.println("\tf filename\t: read input data from filename");
            System.err.println("\td destfilename\t: write output hash to destfilename");
            System.err.println("\tp provider\t: use specific provider");
            System.err.println("\ta algorithm\t: algorithm to use for digest");
            System.err.println("\to\t\t: overwrite destfilename file");
            System.err.println("\to\t\t: overwrite destfilename file");
            System.err.println("\tencode\t\t: BASE64 encode output");
	    return;
        }
        Hash h = new Hash();
        try
        {
            h.parseArgs(args);
            h.run();
        }
        catch (NoSuchProviderException e)
        {
            logger.info("Security Error", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.info("Security Error", e);
        }
        catch (IOException e)
        {
            logger.info("Security Error", e);
        }
    }
    
    public Hash()
    {
        bytesToHash = new byte[ARRAY_INITIAL_SIZE];
        setAlgorithm("MD5");
    }

    public void run() throws NoSuchProviderException, NoSuchProviderException,
            NoSuchAlgorithmException, IOException
    {
        MessageDigest md = createDigestInstance();

        InputStream is = createInputStream(getFileName());
        OutputStream os = createOutputStream(getDestFileName());

        byte[] hashedBytes = digestData(md, is);
        writeBytes(os, hashedBytes);
    }

    /**
     * @param md
     * @param is
     * @param os
     * @throws IOException
     */
    public byte[] digestData(MessageDigest md, InputStream is)
            throws IOException
    {
        bytesToHash = readBytes(is);
        md.update(bytesToHash);
        byte[] hashedBytes = md.digest();
        return hashedBytes;
    }

    /**
     * @return @throws
     *         NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private MessageDigest createDigestInstance()
            throws NoSuchAlgorithmException, NoSuchProviderException
    {
        MessageDigest md;
        if (getProvider() == null)
            md = MessageDigest.getInstance(getAlgorithm());
        else
            md = MessageDigest.getInstance(getAlgorithm(), getProvider());
        return md;
    }

    /**
     * @return Returns the bytesToHash.
     */
    public byte[] getBytesToHash()
    {
        return bytesToHash;
    }

    /**
     * @param bytesToHash
     *            The bytesToHash to set.
     */
    public void setBytesToHash(byte[] bytesToHash)
    {
        this.bytesToHash = bytesToHash;
    }

}
