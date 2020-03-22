/*
 * Created on 17-Aug-2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pluralsight.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;


/**
 * @author kevinj
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class SecurityBase
{

    private String fileName;
    private String destFileName;
    private String algorithm = "MD5";
    private String provider;
    private boolean overwrite = false;
    static final int ARRAY_INITIAL_SIZE = 100;
    private boolean encode = false;

    /**
     * @return Returns the algorithm.
     */
    public String getAlgorithm()
    {
        return algorithm;
    }

    /**
     * @param algorithm
     *            The algorithm to set.
     */
    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

    /**
     * @return Returns the destFileName.
     */
    public String getDestFileName()
    {
        return destFileName;
    }

    /**
     * @param destFileName
     *            The destFileName to set.
     */
    public void setDestFileName(String destFileName)
    {
        this.destFileName = destFileName;
    }

    /**
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName
     *            The fileName to set.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @return Returns the provider.
     */
    public String getProvider()
    {
        return provider;
    }

    /**
     * @param provider
     *            The provider to set.
     */
    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    /**
     * @return Returns the overwrite.
     */
    public boolean isOverwrite()
    {
        return overwrite;
    }

    /**
     * @param overwrite
     *            The overwrite to set.
     */
    public void setOverwrite(boolean overwrite)
    {
        this.overwrite = overwrite;
    }

    /**
     * @return Returns the encode.
     */
    public boolean isEncode()
    {
        return encode;
    }

    /**
     * @param encode
     *            The encode to set.
     */
    public void setEncode(boolean encode)
    {
        this.encode = encode;
    }

    protected void parseArgs(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-f"))
                setFileName(args[++i]);
            else if (args[i].equals("-d"))
                setDestFileName(args[++i]);
            else if (args[i].equals("-p"))
                setProvider(args[++i]);
            else if (args[i].equals("-a"))
                setAlgorithm(args[++i]);
            else if (args[i].equals("-o"))
                setOverwrite(true);
            else if (args[i].equals("-encode")) 
                setEncode(true);
        }
    }

    protected final InputStream createInputStream(String fileName)
            throws FileNotFoundException
    {
        if (fileName == null)
            return System.in;
        else
        {
            File f = new File(fileName);
            if (f.exists())
            {
                return new FileInputStream(f);
            }
            else
            {
                throw new FileNotFoundException();
            }
        }
    }

    protected final OutputStream createOutputStream(String fileName) throws IOException
    {
        if (fileName == null)
            return System.out;
        else
        {
            File f = new File(fileName);

            if (f.exists())
            {
                if (isOverwrite())
                    return new FileOutputStream(f);
                else
                    throw new IOException("Destination file already exists");
            }
            else
            {
                return new FileOutputStream(f);
            }
        }

    }

    protected final byte[] resizeArray(byte[] in)
    {
        int size = in.length;
        byte[] tmp = new byte[in.length * 2];
        System.arraycopy(in, 0, tmp, 0, in.length);
        return tmp;
    }


    /**
     * @param os
     * @throws IOException
     */
    final protected void writeBytes(OutputStream os, byte[] bytes)
            throws IOException
    {
        if(isEncode())
        {
            String temp;
            temp = printBase64Binary(bytes);
            os.write(temp.getBytes());
        }
        else
        {
            os.write(bytes);
        }
        os.flush();
        os.close();
    }

    /**
     * @throws IOException
     *  
     */
    final protected byte[] readBytes(InputStream is) throws IOException
    {
        int offset = 0;
        int bytesRead = 0;
        int size = 0;
        byte[] temp = new byte[ARRAY_INITIAL_SIZE];
        while ((bytesRead = is.read(temp, offset, ARRAY_INITIAL_SIZE)) != -1)
        {
            offset += ARRAY_INITIAL_SIZE;
            while (offset >= (temp.length))
                temp = resizeArray(temp);

            size += bytesRead;
        }

        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++)
        {
            bytes[i] = temp[i];
        }
        is.close();
        return bytes;
    }

}