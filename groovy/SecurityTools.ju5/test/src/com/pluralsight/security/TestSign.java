/*
 * Created on 18-Aug-2004
 *
 */
package com.pluralsight.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;



import junit.framework.TestCase;

/**
 * @author kevinj
 *
 */
public class TestSign extends TestCase
{
    
    String keystoreFileName = "test/testfiles/.keystore";
    String storepass = "storepass";
    String keypass = "keypass";
    String alias = "testkey";
    private static final String RUNFILEIN = "src/com/pluralsight/security/Hash.java";
    private static final String RUNFILEOUT = "runFile";
    File runFileIn;
    File runFileOut;

    protected void setUp() throws Exception
    {
        File f = new File(keystoreFileName);
        
        if(f.exists() == false)
            throw new FileNotFoundException();

        runFileIn = new File(RUNFILEIN);
        runFileOut = new File(RUNFILEOUT);
    }

    public void testLoadKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException
    {
        Sign s = new Sign();
        
        s.setKeystoreType("JKS");
        s.setKeystoreFileName(keystoreFileName);
        s.setKeyStorePass(storepass.toCharArray());
        KeyStore keystore = s.loadKeyStore();
        
        assertNotNull(keystore);
    }
    
    public void testLoadPrivateKey() throws CertificateException, FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
    {
        Sign s = new Sign();
        
        s.setKeystoreType("JKS");
        s.setKeystoreFileName(keystoreFileName);
        KeyStore keystore = s.loadKeyStore();
        s.setKeystore(keystore);
        s.setKeyPassword(keypass.toCharArray());
        s.setKeyAlias(alias);
        
        PrivateKey pk = s.loadPrivateKey();
        
        assertNotNull(pk);
        
    }

    public void testLoadPrivateKeyInvalidPassword() throws CertificateException, FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException
    {
        Sign s = new Sign();
        
        s.setKeystoreType("JKS");
        s.setKeystoreFileName(keystoreFileName);
        KeyStore keystore = s.loadKeyStore();
        s.setKeystore(keystore);
        s.setKeyPassword("".toCharArray());
        s.setKeyAlias(alias);
        
        PrivateKey pk;
        try
        {
            pk = s.loadPrivateKey();
        }
        catch (UnrecoverableKeyException e)
        {
            return;
        }
        fail();
    }

    public void testCreateSignature() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException
    {
        Sign s = new Sign();
        
        
        s.setKeystoreType("JKS");
        s.setKeystoreFileName(keystoreFileName);
        KeyStore keystore = s.loadKeyStore();
        s.setKeystore(keystore);
        
        s.setAlgorithm("DSA");
        Signature sign = s.createSignatureInstance();
        
        assertNotNull(sign);
    }
    
    public void testSignData() throws 
    	InvalidKeyException, SignatureException, IOException, NoSuchProviderException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException
    {
        Sign s = new Sign();

        s.setKeystoreFileName(keystoreFileName);
        KeyStore keystore = s.loadKeyStore();
        s.setKeystore(keystore);
        s.setKeyPassword(keypass.toCharArray());
        s.setKeyAlias(alias);
        PrivateKey pk = s.loadPrivateKey();

        byte[] bytesIn = createBytesToSign(50);

        
        Signature signature = s.createSignatureInstance();
        signature.initSign(pk);
        byte[] realSignedBytes = s.signData(signature, bytesIn);
        

        signature = s.createSignatureInstance();
        PublicKey pubkey = s.loadPublicKey();
        signature.initVerify(pubkey);
        
        signature.update(bytesIn);
        assertTrue(signature.verify(realSignedBytes));
    }

    public void testVerifyData() throws 
		InvalidKeyException, SignatureException, IOException, NoSuchProviderException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException
	{
        Sign s = new Sign();
        s.setKeystoreFileName(keystoreFileName);
        KeyStore keystore = s.loadKeyStore();
        s.setKeystore(keystore);
        s.setKeyPassword(keypass.toCharArray());
        s.setKeyAlias(alias);

        Signature sig = Signature.getInstance("DSA");
        sig.initSign((PrivateKey)keystore.getKey(alias, keypass.toCharArray()));
        byte[] bytesToVerify = createBytesToSign(100); 
        sig.update(bytesToVerify);
        byte[] signedBytes = sig.sign();
                       
        s.verifyData(sig, bytesToVerify, signedBytes);
	}

//    public void testVerifyDataWithDecode() throws
//    	InvalidKeyException, SignatureException, IOException, NoSuchProviderException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException
//	{
//	    Sign s = new Sign();
//	    s.setKeystoreFileName(keystoreFileName);
//	    KeyStore keystore = s.loadKeyStore();
//	    s.setKeystore(keystore);
//	    s.setKeyPassword(keypass.toCharArray());
//	    s.setKeyAlias(alias);
//
//	    Signature sig = Signature.getInstance("DSA");
//	    sig.initSign((PrivateKey)keystore.getKey(alias, keypass.toCharArray()));
//	    byte[] bytesToVerify = createBytesToSign(100);
//	    sig.update(bytesToVerify);
//	    byte[] signedBytes = sig.sign();
//
//
//	    String temp = printBase64Binary(signedBytes);
//	    s.setDecode(true);
//	    s.verifyData(sig, bytesToVerify, temp.getBytes());
//	}

    private ByteArrayInputStream createInputStream(byte[] bytes)
    {
        return new ByteArrayInputStream(bytes);
    }
    
    private ByteArrayOutputStream createOutputStream()
    {
        return new ByteArrayOutputStream();
    }
    
    private byte[] createBytesToSign(int size)
    {
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++)
        {
            bytes[i] = (byte)i;
        }
        return bytes;
    }

    public void testParseArgs()
    {
        Sign s = new Sign();
        
        String args[] = {"-storepass", "storepass", "-keystore", "keystore", "-keystoretype", "keystoretype", "-sigfilename", "sigfilename", "-keypass", "keypass", "-alias", "alias", "-decode", "-sigfilename", "sigfilename"};
        
        s.parseArgs(args);
        
        assertEquals("storepass", new String(s.getKeyStorePass()));
        assertEquals("keystore", s.getKeyStoreFilename());
        assertEquals("keystoretype", s.getKeystoreType());
        assertEquals("sigfilename", s.getSignatureFileName());
        assertEquals("keypass", new String(s.getKeyPassword()));
        assertEquals("alias", s.getKeyAlias());
        assertEquals("sigfilename", s.getSignatureFileName());
        assertEquals(true, s.isDecode());
    }
    
    public void testParseArgsDefaults()
    {
        Sign s = new Sign();
        
        String args[] = {""};
        
        s.parseArgs(args);
        
        assertNull(s.getKeyStorePass());
        assertNull(s.getKeyStoreFilename());
        assertEquals("JKS", s.getKeystoreType());
        assertNull(s.getSignatureFileName());
        assertNull(s.getKeyPassword());
        assertNull(s.getKeyAlias());
        assertNull(s.getSignatureFileName());
        assertEquals(false, s.isDecode());
    }
    
    protected void tearDown() throws Exception
    {
        runFileOut.delete();
    }
}
